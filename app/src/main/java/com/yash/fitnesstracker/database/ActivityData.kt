package com.yash.fitnesstracker.database

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.outlined.RunCircle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Activity(
    val name:String,
    val met:Double,
    val icon: ImageVector = Icons.Default.FitnessCenter,
    val color : Color = Color(0xFF2EC4B6)
)

val Activities = listOf(
    Activity("Running",7.0, Icons.Outlined.RunCircle),
    Activity("Cycling(Outdoor)",6.0, Icons.AutoMirrored.Filled.DirectionsBike),
    Activity("Cycling(Indoor)",6.8,Icons.Default.PedalBike),
    Activity("Treadmill",6.0, Icons.AutoMirrored.Filled.DirectionsRun),
    Activity("Jump Rope",10.0,Icons.Default.SportsGymnastics),
    Activity("Swimming",9.5,Icons.Default.Water),
    Activity("Dancing",6.0,Icons.Default.SelfImprovement),
    Activity("Push-ups/Squats",6.0,Icons.Default.AccessibilityNew),
    Activity("Pull-ups/Chin-ups",8.0,Icons.Default.AccessibilityNew),
    Activity("Gym",3.5,Icons.Default.FitnessCenter)
)