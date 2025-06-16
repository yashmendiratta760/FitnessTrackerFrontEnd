package com.yash.fitnesstracker.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.yash.fitnesstracker.API.ServerDbApi
import com.yash.fitnesstracker.MainApplication
import com.yash.fitnesstracker.database.StepsDTO
import com.yash.fitnesstracker.database.StepsEntities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object StepRepository {

    private lateinit var appContainer: AppContainer

    fun initialize(context: Context) {
        appContainer = (context.applicationContext as MainApplication).container
    }
    private val stepsLocalDbRepository
        get() = appContainer.stepsLocalDbRepository


    private val _stepCount = MutableStateFlow(0)
    val stepCount: StateFlow<Int> = _stepCount

    fun updateSteps(steps: Int) {
        _stepCount.value = steps
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertOrUpdateInRoom(steps:Int)
    {

        val date = LocalDate.now()
        val formattedDate = date.format(DateTimeFormatter.ISO_DATE)

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



}