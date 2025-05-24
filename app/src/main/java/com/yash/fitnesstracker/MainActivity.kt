package com.yash.fitnesstracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yash.fitnesstracker.Login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.navigation.navigate
import com.yash.fitnesstracker.ui.theme.FitnessTrackerTheme
import com.yash.fitnesstracker.viewmodel.appViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appViewModel = androidx.lifecycle.ViewModelProvider(this, appViewModel.Factory).get(appViewModel::class.java)
        appViewModel.scheduleDailyStepUpload(this)

        enableEdgeToEdge()
        setContent {
            FitnessTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val LoginSignupViewModel: LoginSignupViewModel = viewModel(factory = LoginSignupViewModel.Factory)
                    navigate(modifier = Modifier.padding(innerPadding),
                        loginSignupViewModel = LoginSignupViewModel,
                        appViewModel = appViewModel)
                }
            }
        }
    }
}
