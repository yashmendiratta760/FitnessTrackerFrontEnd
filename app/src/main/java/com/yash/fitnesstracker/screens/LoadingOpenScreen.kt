package com.yash.fitnesstracker.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.Service.DataStoreManager
import com.yash.fitnesstracker.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val userName = DataStoreManager.getUserName(context)
        delay(1000) // Optional: gives smooth transition
        if (!userName.isNullOrBlank()) {
            navController.navigate(Screens.Home.name) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController.navigate(Screens.Login.name) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // Simple UI while checking login state
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Fitness Tracker", style = MaterialTheme.typography.headlineMedium)
    }
}
