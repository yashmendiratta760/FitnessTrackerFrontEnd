package com.yash.fitnesstracker.viewmodel


import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yash.fitnesstracker.MainApplication
import com.yash.fitnesstracker.service.AutoWorkManager
import com.yash.fitnesstracker.service.DataStoreManager
import com.yash.fitnesstracker.database.ActivityDTO
import com.yash.fitnesstracker.database.ActivityHistoryEntities
import com.yash.fitnesstracker.database.StepsDTO
import com.yash.fitnesstracker.database.StepsEntities
import com.yash.fitnesstracker.repository.ActivityHistoryRepo
import com.yash.fitnesstracker.repository.OnlineServerDbRep
import com.yash.fitnesstracker.repository.StepRepository
import com.yash.fitnesstracker.repository.StepsLocalDbRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit


@RequiresApi(Build.VERSION_CODES.O)
class AppViewModel(private val stepsLocalDbRepository: StepsLocalDbRepository,
    private val onlineServerDbRep: OnlineServerDbRep,
    private val activityHistoryRepo: ActivityHistoryRepo): ViewModel()
{

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            StepRepository.stepCount.collectLatest { steps ->
                _uiState.value = _uiState.value.copy(steps = steps)
            }
        }
    }

    fun getAllStepsFromLocalDb()
    {
        viewModelScope.launch {
            val allStepData = stepsLocalDbRepository.getAllData()
            val steps = mutableListOf<StepsDTO>()
            allStepData?.forEach { data->
                val formattedDate = data.date.format(DateTimeFormatter.ISO_DATE)
                steps.add(StepsDTO(data.steps.toString(),formattedDate))
            }
            _uiState.value = _uiState.value.copy(stepsData = steps)

        }

    }

    fun getAllSteps()
    {
        Log.d("ServerFetchSteps","Start")
        viewModelScope.launch {
            val response = onlineServerDbRep.getAllData()
            if (response.isSuccessful) {
                val stepsData = response.body() ?: emptyList()
                Log.d("getAllSteps", "Fetched: ${stepsData.size} entries")
                stepsData.forEach { serverData ->
                    val localStep = stepsLocalDbRepository.getStepsByDate(serverData.date)
                    if (localStep == null) {
                        val formattedDate = serverData.date.format(DateTimeFormatter.ISO_DATE)
                        insertOrUpdateInRoom(serverData.steps.toInt(), formattedDate)
                    }
                }
            } else {
                Log.e("getAllSteps", "API failed: ${response.code()} ${response.message()}")
            }


        }
    }

    fun syncStepsOncePerDay() {
        viewModelScope.launch {
            val lastSyncDate = DataStoreManager.getLastSyncDate()
            if (!DataStoreManager.isToday(lastSyncDate)) {
                Log.d("Server call","Stress")
                getAllSteps() // <- Your existing server sync function
                val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                DataStoreManager.setLastSyncDate(today)
            }
        }
    }



    suspend fun insertOrUpdateInRoom(steps:Int,formattedDate: String)
    {

//        val date = LocalDate.now()
//        val formattedDate = date.format(DateTimeFormatter.ISO_DATE)

        val existingEntity = stepsLocalDbRepository.getStepsByDate(formattedDate)
        if (existingEntity != null) {
            // If exists, update the steps
            val updatedEntity = existingEntity.copy(steps = steps)
            stepsLocalDbRepository.update(updatedEntity)
        } else {
            // If not exists, insert a new entity
            val newEntity = StepsEntities(steps = steps, date = formattedDate)
            stepsLocalDbRepository.insert(newEntity)
        }
    }


    fun scheduleDailyStepUpload(context: Context) {

        val currentDateTime = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, 0)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)

        if (dueDate.before(currentDateTime)) {
            dueDate.add(Calendar.DATE, 1)
        }

        val timeDiff = dueDate.timeInMillis - currentDateTime.timeInMillis

        val workRequest = PeriodicWorkRequestBuilder<AutoWorkManager>(1, TimeUnit.DAYS)
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)  // delay until next midnight
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork("dailyUpload", ExistingPeriodicWorkPolicy.REPLACE,workRequest)
    }

    fun getActivityHistory()
    {
        viewModelScope.launch {
            val activity = activityHistoryRepo.getData()

            _uiState.value = _uiState.value.copy(activityHistory = activity)
        }
    }

    fun addActivity(activityDTO: ActivityDTO) {
        viewModelScope.launch {
            val entity = ActivityHistoryEntities(
                name = activityDTO.name,
                time = activityDTO.time,
                cal = activityDTO.cal
            )
            activityHistoryRepo.insert(entity)
            delay(500L)
            getActivityHistory()

        }
    }


    fun uploadImage(image: MultipartBody.Part,userName: RequestBody)
    {
        viewModelScope.launch {
            try {
                val response = onlineServerDbRep.uploadAndGetImage(image, userName)
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.imageUrl
                    _uiState.value = _uiState.value.copy(imageUrl = imageUrl)
                    Log.d("UPLOAD", "Image uploaded: $imageUrl")
                } else {
                    Log.e("UPLOAD", "Upload failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("UPLOAD", "Exception: ${e.message}")
            }
        }
    }

    fun getImageUrl()
    {
        viewModelScope.launch {
            try {
                val response = onlineServerDbRep.getImageUrl()
                if(response.code()==200)
                {
                    val imageUrl = response.body()
                    _uiState.value = _uiState.value.copy(imageUrl=imageUrl)
                    Log.d("imageUrl",imageUrl.toString())
                }
                else
                {
                    Log.d("imageUrl","Not found")
                }
            } catch (e: Exception) {
                Log.e("error",e.toString())
            }
        }
    }








    companion object{
        val Factory: ViewModelProvider.Factory= viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as MainApplication
                val repository = application.container.stepsLocalDbRepository
                val repositoryServerDb = application.container.onlineServerDbRep
                val activityHistoryRepo = application.container.activityHistoryRepo
                AppViewModel(repository,repositoryServerDb,
                    activityHistoryRepo)
            }
        }
    }


}