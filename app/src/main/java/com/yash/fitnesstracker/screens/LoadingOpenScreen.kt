package com.yash.fitnesstracker.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.Service.DataStoreManager
import com.yash.fitnesstracker.navigation.Screens
import com.yash.fitnesstracker.repository.TokenManager
import com.yash.fitnesstracker.viewmodel.AppUiState
import com.yash.fitnesstracker.viewmodel.appViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SplashScreen(navController: NavHostController,
                 appViewModel: appViewModel,
                 uiState: AppUiState) {
    val context = LocalContext.current



    var token by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        token = TokenManager.getToken(context)
    }


    LaunchedEffect(Unit) {
        val userName = DataStoreManager.getUserName(context)
        delay(1000) // Optional: gives smooth transition
        if (token!=null) {
            navController.navigate(Screens.Home.name) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController.navigate(Screens.Login.name) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit){
        appViewModel.getActivityHistory()
        appViewModel.getImageUrl()
    }

    // Simple UI while checking login state
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Fitness Tracker", style = MaterialTheme.typography.headlineMedium)
    }
}
