package com.yash.fitnesstracker.repository

import android.util.Log
import com.yash.fitnesstracker.API.ServerDbApi
import com.yash.fitnesstracker.database.StepsDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object StepRepository {
    private val _stepCount = MutableStateFlow(0)
    val stepCount: StateFlow<Int> = _stepCount

    fun updateSteps(steps: Int) {
        _stepCount.value = steps
    }

}