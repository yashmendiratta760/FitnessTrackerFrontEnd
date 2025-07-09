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
import com.yash.fitnesstracker.Service.DataStoreManager
import com.yash.fitnesstracker.navigation.Navigate
import com.yash.fitnesstracker.screens.components.DropDownOption
import com.yash.fitnesstracker.screens.components.StopWatch
import com.yash.fitnesstracker.ui.theme.FitnessTrackerTheme
import com.yash.fitnesstracker.viewmodel.UserViewModel
import com.yash.fitnesstracker.viewmodel.appViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataStoreManager.init(this)
        val appViewModel = androidx.lifecycle.ViewModelProvider(this, appViewModel.Factory).get(appViewModel::class.java)

        appViewModel.scheduleDailyStepUpload(this)

        enableEdgeToEdge()
        setContent {
            FitnessTrackerTheme {
//                StopWatch()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val userViewModel : UserViewModel = viewModel()
                    val LoginSignupViewModel: LoginSignupViewModel = viewModel(factory = LoginSignupViewModel.Factory)
                    Navigate(modifier = Modifier.padding(innerPadding),
                        loginSignupViewModel = LoginSignupViewModel,
                        appViewModel = appViewModel,
                        userViewModel = userViewModel)
                }
//                DropDownOption()
//                BarGraph(data = listOf(10,20,30,40,50,60,40),listOf("Mon", "Tue", "Wed", "Thu", "Fri","Sat","Sun"))
            }
        }
    }
}
