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
import com.yash.fitnesstracker.service.DataStoreManager
import com.yash.fitnesstracker.navigation.Navigate
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
