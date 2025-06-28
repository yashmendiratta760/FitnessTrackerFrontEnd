package com.yash.fitnesstracker.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.yash.fitnesstracker.MainApplication
import com.yash.fitnesstracker.database.StepsEntities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object StepRepository {

    private lateinit var appContainer: AppContainer

    fun initialize(context: Context) {
        appContainer = (context.applicationContext as MainApplication).container
    }
    private val stepsLocalDbRepository
        get() = appContainer.stepsLocalDbRepository

    private val _time = MutableStateFlow(0L)
    val timeCount : StateFlow<Long> = _time

    fun updateTime(time: Long)
    {
        _time.value = time
    }


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