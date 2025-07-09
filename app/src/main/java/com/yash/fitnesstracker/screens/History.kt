package com.yash.fitnesstracker.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.outlined.RunCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.navigation.Screens
import com.yash.fitnesstracker.screens.components.CustomisedTopBar
import com.yash.fitnesstracker.screens.components.CustomizedButton
import com.yash.fitnesstracker.screens.components.DetailsTile
import com.yash.fitnesstracker.utils.bg
import com.yash.fitnesstracker.viewmodel.AppUiState

@Composable
fun History(navController : NavHostController,
            uiState: AppUiState)
{
    Scaffold(topBar = {
        CustomisedTopBar(
            title = Screens.History.name,
            navController = navController
        )
    }) {
        bg()
        Column(modifier = Modifier.padding(it)) {


            if (!uiState.activityHistory.isEmpty()) {
                LazyColumn {
                    items(uiState.activityHistory) {activity->
                        val icon = when (activity.name) {
                            "Running" -> Icons.Outlined.RunCircle
                            "Cycling(Outdoor)" -> Icons.Default.DirectionsBike
                            "Cycling(Indoor)" -> Icons.Default.PedalBike
                            "Treadmill" -> Icons.Default.DirectionsRun
                            "Jump Rope" -> Icons.Default.SportsGymnastics
                            "Swimming" -> Icons.Default.Water
                            "Dancing" -> Icons.Default.SelfImprovement
                            "Push-ups/Squats" -> Icons.Default.AccessibilityNew
                            "Pull-ups/Chin-ups" -> Icons.Default.AccessibilityNew
                            "Gym" -> Icons.Default.FitnessCenter
                            else -> Icons.Default.FitnessCenter // fallback
                        }
                        DetailsTile(modifier = Modifier.padding(5.dp),
                            text = activity.name,
                            time = activity.time,
                            cal  = activity.cal,
                            icon = icon,
                            iconColor = Color.Cyan)
                    }

                }
            }
            else
            {
                Box(Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    Text(
                        text = "No History Found",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}
