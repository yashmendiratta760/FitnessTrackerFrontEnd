package com.yash.fitnesstracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash.fitnesstracker.login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.service.DataStoreManager
import com.yash.fitnesstracker.navigation.Navigate
import com.yash.fitnesstracker.service.ForegroundService
import com.yash.fitnesstracker.ui.theme.FitnessTrackerTheme
import com.yash.fitnesstracker.viewmodel.UserViewModel
import com.yash.fitnesstracker.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataStoreManager.init(this)
        val appViewModel = androidx.lifecycle.ViewModelProvider(this, AppViewModel.Factory)[AppViewModel::class.java]

        appViewModel.scheduleDailyStepUpload(this)

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            FitnessTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val userViewModel : UserViewModel = viewModel()
                    val loginSignupViewModel: LoginSignupViewModel = viewModel(factory = LoginSignupViewModel.Factory)
                    Navigate(
                        modifier = Modifier.padding(innerPadding),
                        loginSignupViewModel = loginSignupViewModel,
                        appViewModel = appViewModel,
                        userViewModel = userViewModel)
                }
            }
        }
    }
}
