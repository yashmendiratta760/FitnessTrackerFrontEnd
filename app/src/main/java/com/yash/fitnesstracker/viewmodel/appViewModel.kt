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
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.yash.fitnesstracker.MainApplication
import com.yash.fitnesstracker.Service.AutoWorkManager
import com.yash.fitnesstracker.database.StepsDTO
import com.yash.fitnesstracker.database.StepsEntities
import com.yash.fitnesstracker.repository.OnlineServerDbRep
import com.yash.fitnesstracker.repository.StepRepository
import com.yash.fitnesstracker.repository.StepsLocalDbRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit


@RequiresApi(Build.VERSION_CODES.O)
class appViewModel(private val stepsLocalDbRepository: StepsLocalDbRepository,
    private val onlineServerDbRep: OnlineServerDbRep): ViewModel()
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



//    fun insertOrUpdateInRoom(steps:Int)
//    {
//        viewModelScope.launch {
//            val date = LocalDate.now()
//            val formattedDate = date.format(DateTimeFormatter.ISO_DATE)
//
//            val existingEntity = stepsLocalDbRepository.getStepsByDate(formattedDate)
//            if (existingEntity != null) {
//                // If exists, update the steps
//                val updatedEntity = existingEntity.copy(steps = steps)
//                stepsLocalDbRepository.update(updatedEntity)
//            } else {
//                // If not exists, insert a new entity
//                val newEntity = StepsEntities(steps = steps, date = formattedDate)
//                stepsLocalDbRepository.insert(newEntity)
//            }
//
//        }
//    }

    fun sendStepsToServerDb(steps:String)
    {
        viewModelScope.launch {
            val date = LocalDate.now()
            val formattedDate = date.format(DateTimeFormatter.ISO_DATE)
            val data = StepsDTO(steps,formattedDate)

            val response = onlineServerDbRep.sendSteps(data)
            if(response.code()==200) {
                Log.d("ServerDb", "Data Uploaded")

            }
            else{
                Log.d("ServerDb","Data upload failed")

            }

        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory= viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as MainApplication
                val repository = application.container.stepsLocalDbRepository
                val repositoryServerDb = application.container.onlineServerDbRep
                appViewModel(repository,repositoryServerDb)
            }
        }
    }


}