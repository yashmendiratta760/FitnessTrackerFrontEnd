package com.yash.fitnesstracker.navigation

import android.R.attr.defaultValue
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yash.fitnesstracker.Login_Signup.Screens.Login
import com.yash.fitnesstracker.Login_Signup.Screens.Otp
import com.yash.fitnesstracker.Login_Signup.Screens.SignUp
import com.yash.fitnesstracker.Login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.screens.HomeScreen
import com.yash.fitnesstracker.viewmodel.appViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.yash.fitnesstracker.screens.DailyHistory
import com.yash.fitnesstracker.screens.WeeklyHistory
import com.yash.fitnesstracker.repository.TokenManager
import com.yash.fitnesstracker.screens.ActivityRunningScreen
import com.yash.fitnesstracker.screens.ActivityScreen
import com.yash.fitnesstracker.screens.History
import com.yash.fitnesstracker.screens.SplashScreen
import com.yash.fitnesstracker.screens.UserProfileScreen
import com.yash.fitnesstracker.screens.components.NavigationDrawer
import com.yash.fitnesstracker.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigate(navController:NavHostController= rememberNavController(),modifier: Modifier,
             appViewModel: appViewModel,loginSignupViewModel: LoginSignupViewModel,
             userViewModel: UserViewModel)

{
    val userUiState by userViewModel.uiState.collectAsState()

    val uiState by appViewModel.uiState.collectAsState()

    val loginSignupUiState by loginSignupViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val startDestination = Screens.Splash.name
    NavHost(navController=navController, startDestination = startDestination) {
        composable(Screens.Splash.name) {
            SplashScreen(navController=navController,
                appViewModel=appViewModel,
                uiState=uiState)
        }
        composable(route = Screens.Signup.name) {
            SignUp(navController = navController, loginSignupViewModel)
        }
        composable(route= Screens.Login.name) {
            Login(navController, loginSignupViewModel,loginSignupUiState)
        }
        composable(route = Screens.Otp.name) {
            Otp(loginSignupViewModel,navController)
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
            NavigationDrawer(appViewModel,loginSignupViewModel,userViewModel,navController,drawerState,uiState,
                userUiState = userUiState)
        }
        composable(route = Screens.DailyHistory.name,enterTransition = {
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
            DailyHistory(navController=navController)
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
            UserProfileScreen(navController=navController, userViewModel = userViewModel, appViewModel = appViewModel, appUiState = uiState)
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
            ActivityScreen(navController=navController)

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
                uiState = uiState)
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
            val ScreenName = backStackEntry.arguments?.getString("ScreenName") ?: "None"
            val Met = backStackEntry.arguments?.getString("Met")?.toDoubleOrNull() ?: 0.0
            val icon = when (ScreenName) {
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

            ActivityRunningScreen(navController = navController,
                screenName = ScreenName,icon = icon,Met = Met,
                appViewModel = appViewModel)
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
            WeeklyHistory(navController=navController,appViewModel)
        }
    }
}