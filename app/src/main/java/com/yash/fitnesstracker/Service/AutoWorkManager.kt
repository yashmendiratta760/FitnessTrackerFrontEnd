package com.yash.fitnesstracker.Service

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yash.fitnesstracker.API.ServerDbApi
import com.yash.fitnesstracker.MainApplication
import com.yash.fitnesstracker.database.StepsDTO
import com.yash.fitnesstracker.repository.OnlineServerDbRep
import com.yash.fitnesstracker.viewmodel.AppUiState
import com.yash.fitnesstracker.viewmodel.appViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AutoWorkManager(context: Context,workerParameters: WorkerParameters): CoroutineWorker(context,workerParameters)
{
    private val appContainer = (applicationContext as MainApplication).container

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {


        val date = LocalDate.now().minusDays(1)
        val formattedDate = date.format(DateTimeFormatter.ISO_DATE)
        val yesterday = appContainer.stepsLocalDbRepository.getStepsByDate(formattedDate)

        val stepsString = yesterday?.steps

        Log.d("AutoWorkManager", "Steps to upload: $stepsString")

        val data = StepsDTO(stepsString.toString(),formattedDate)


        val response = appContainer.onlineServerDbRep.sendSteps(steps = data)

        return if(response.code()==200) {
            Log.d("ServerDb", "Data Uploaded")
            Result.success()
        }
        else{
            Log.d("ServerDb","Data upload failed")
            Result.failure()
        }
    }

}