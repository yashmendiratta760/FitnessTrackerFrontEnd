package com.yash.fitnesstracker.viewmodel

import com.yash.fitnesstracker.database.ActivityDTO
import com.yash.fitnesstracker.database.StepsDTO
import com.yash.fitnesstracker.database.StepsEntities

data class AppUiState(
    var signup: Boolean = true,
    var steps: Int=0,
    val stepsData: MutableList<StepsDTO> = mutableListOf<StepsDTO>(),
    var activityHistory: List<ActivityDTO> = mutableListOf(),
    val imageUrl : String? = ""
)
