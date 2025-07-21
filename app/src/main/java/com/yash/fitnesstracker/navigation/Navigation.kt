package com.yash.fitnesstracker.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yash.fitnesstracker.login_Signup.screens.Login
import com.yash.fitnesstracker.login_Signup.screens.Otp
import com.yash.fitnesstracker.login_Signup.screens.SignUp
import com.yash.fitnesstracker.login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.screens.ActivityRunningScreen
import com.yash.fitnesstracker.screens.ActivityScreen
import com.yash.fitnesstracker.screens.History
import com.yash.fitnesstracker.screens.SplashScreen
import com.yash.fitnesstracker.screens.UserProfileScreen
import com.yash.fitnesstracker.screens.WeeklyHistory
import com.yash.fitnesstracker.screens.components.NavigationDrawer
import com.yash.fitnesstracker.viewmodel.AppViewModel
import com.yash.fitnesstracker.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigate(modifier: Modifier= Modifier,
             navController:NavHostController= rememberNavController(),
             appViewModel: AppViewModel, loginSignupViewModel: LoginSignupViewModel,
             userViewModel: UserViewModel,
             isDarkTheme: Boolean = isSystemInDarkTheme()
)

{
    val userUiState by userViewModel.uiState.collectAsState()

    val appUiState by appViewModel.uiState.collectAsState()

    val loginSignupUiState by loginSignupViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val startDestination = Screens.Splash.name
    NavHost(navController=navController, startDestination = startDestination) {
        composable(Screens.Splash.name) {
            SplashScreen(navController=navController,
                appViewModel=appViewModel)
        }
        composable(route = Screens.Signup.name) {
            SignUp(navController = navController, loginSignupViewModel,
                loginSignupUiState=loginSignupUiState)
        }
        composable(route= Screens.Login.name) {
            Login(navController, loginSignupViewModel,loginSignupUiState, appViewModel = appViewModel)
        }
        composable(route = Screens.Otp.name) {
            Otp(loginSignupViewModel,navController,
                loginSignupUiState=loginSignupUiState)
        }
        composable(route = Screens.Home.name,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(500)
                )
            }){
            NavigationDrawer(appViewModel=appViewModel,
                loginSignupViewModel=loginSignupViewModel,
                userViewModel=userViewModel,
                navController=navController,
                drawerState=drawerState,
                appUiState = appUiState,
                userUiState = userUiState,
                isDarkTheme=isDarkTheme)
        }

        composable(route = Screens.Profile.name,enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(500)
            )
        },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                )
            }
        ) {
            UserProfileScreen(navController=navController,
                userViewModel = userViewModel,
                appViewModel = appViewModel,
                appUiState = appUiState,
                isDarkTheme = isDarkTheme)
        }

        composable(route = Screens.Activity.name,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                )
            }
        ) {
            ActivityScreen(navController=navController,
                isDarkTheme=isDarkTheme)

        }

        composable(route = Screens.History.name,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                )
            } )
        {

            History(navController=navController,
                uiState = appUiState,
                isDarkTheme=isDarkTheme)
        }


        composable(route = "${Screens.ActivityRunning.name}?ScreenName={ScreenName}&Met={Met}",
            arguments = listOf(
                navArgument("ScreenName") {
                    type = NavType.StringType
                    defaultValue = "None"
                },
                navArgument("Met") {
                    type = NavType.StringType
                    defaultValue = "None"
                }
            )
            ,enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                )
            })
        {backStackEntry->
            val screenName = backStackEntry.arguments?.getString("ScreenName") ?: "None"
            val met = backStackEntry.arguments?.getString("Met")?.toDoubleOrNull() ?: 0.0
            val icon = when (screenName) {
                "Running" -> Icons.Outlined.RunCircle
                "Cycling(Outdoor)" -> Icons.AutoMirrored.Filled.DirectionsBike
                "Cycling(Indoor)" -> Icons.Default.PedalBike
                "Treadmill" -> Icons.AutoMirrored.Filled.DirectionsRun
                "Jump Rope" -> Icons.Default.SportsGymnastics
                "Swimming" -> Icons.Default.Water
                "Dancing" -> Icons.Default.SelfImprovement
                "Push-ups/Squats" -> Icons.Default.AccessibilityNew
                "Pull-ups/Chin-ups" -> Icons.Default.AccessibilityNew
                "Gym" -> Icons.Default.FitnessCenter
                else -> Icons.Default.FitnessCenter // fallback
            }

            ActivityRunningScreen(navController = navController,
                screenName = screenName,icon = icon,met = met,
                appViewModel = appViewModel,
                userDataState = userUiState)
        }
        composable(route = Screens.WeeklyHistory.name,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(500)
                )
            }) {
            WeeklyHistory(navController=navController,
                appUiState = appUiState,
                isDarkTheme = isDarkTheme)
        }
    }
}