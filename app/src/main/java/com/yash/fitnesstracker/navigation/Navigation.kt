package com.yash.fitnesstracker.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yash.fitnesstracker.Login_Signup.Screens.Login
import com.yash.fitnesstracker.Login_Signup.Screens.OpenPage
import com.yash.fitnesstracker.Login_Signup.Screens.Otp
import com.yash.fitnesstracker.Login_Signup.Screens.SignUp
import com.yash.fitnesstracker.Login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.Screens.HomeScreen
import com.yash.fitnesstracker.viewmodel.appViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.yash.fitnesstracker.repository.TokenManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun navigate(navController:NavHostController= rememberNavController(),modifier: Modifier,
             appViewModel: appViewModel,loginSignupViewModel: LoginSignupViewModel)
{

    val loginSignupUiState by loginSignupViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var token by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        token = TokenManager.getToken(context)
    }
    val startDestination = if (token.isNullOrEmpty()) "login" else "home"
    NavHost(navController=navController, startDestination = startDestination) {
        composable(route="open") {
            OpenPage(navController)
        }
        composable(route = "Signup") {
            SignUp(navController = navController, loginSignupViewModel)
        }
        composable(route="login") {
            Login(navController, loginSignupViewModel,loginSignupUiState)
        }
        composable(route = "otp") {
            Otp(loginSignupViewModel,navController)
        }
        composable(route="home"){
            HomeScreen(appViewModel = appViewModel, loginSignupViewModel = loginSignupViewModel,
                navController = navController)
        }
    }
}