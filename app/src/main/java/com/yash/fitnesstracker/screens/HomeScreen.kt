package com.yash.fitnesstracker.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.navigation.Screens
import com.yash.fitnesstracker.screens.components.CircularProgress
import com.yash.fitnesstracker.screens.components.CustomizedButton
import com.yash.fitnesstracker.service.DataStoreManager
import com.yash.fitnesstracker.service.ForegroundService
import com.yash.fitnesstracker.utils.Bg
import com.yash.fitnesstracker.viewmodel.AppUiState
import com.yash.fitnesstracker.viewmodel.UserViewModel
import com.yash.fitnesstracker.viewmodel.AppViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat

//suspend fun simulateMidnightBase(context: Context,steps:Int) {
//    // Pretend it's midnight now
//    DataStoreManager.saveMidnightBase(context, steps)
//}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(modifier: Modifier= Modifier,
               appViewModel: AppViewModel,
               userViewModel: UserViewModel,
               navController: NavHostController,
               drawerState: DrawerState,
               appUiState: AppUiState,
               isDarkTheme: Boolean )
{

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        appViewModel.syncStepsOncePerDay()

    }
//    LaunchedEffect(Unit) {
//        simulateMidnightBase(context,26880)
//    }



//    LaunchedEffect(showDetail.value) {
//        if (showDetail.value) {
//            drawerState.open()
//        }
//    }

    var elapsedTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        elapsedTime = DataStoreManager.getTime(context)
    }

    val steps = appUiState.steps
    Log.d("UI", "Steps from ViewModel: $steps")
    val userUiState = userViewModel.uiState.collectAsState()
    val stepsGoal = userUiState.value.stepsGoal.toFloatOrNull() ?: 0f
    val progress = if (stepsGoal > 0f) steps / stepsGoal else 0f
    val safeProgress = progress.coerceIn(0f, 1f)


    LaunchedEffect(steps) {
        if(steps<0)
        {
            DataStoreManager.saveMidnightBase(context,0)
        }
    }

    LaunchedEffect(Unit) {
        // ✅ Check if both permissions are granted
        val hasActivityRecognition = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED

        val hasHealthPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.FOREGROUND_SERVICE_HEALTH
            ) == PackageManager.PERMISSION_GRANTED
        } else true

        // ✅ If granted, start foreground service
        if (hasActivityRecognition && hasHealthPermission) {
            val intent = Intent(context, ForegroundService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
    }


    Box {
        Bg()
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, end = 30.dp), horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = modifier
                        .padding(start = 16.dp)
                        .clip(
                            RoundedCornerShape(30.dp)
                        )

                        .clickable(onClick = { coroutineScope.launch {
                            if(drawerState.isClosed) drawerState.open()
                        }}),
                    contentAlignment = Alignment.TopStart
                ) {
                    Icon(imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        modifier= Modifier.size(25.dp))
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(170.dp)
                        ) {
                            CircularProgress(progress = safeProgress, modifier = Modifier.fillMaxSize(),
                                isDarkTheme=isDarkTheme)
                            Text(text = steps.toString(), fontSize = 40.sp)
                        }

                    }
                }
                item {
                    val color by remember {
                        mutableStateOf(
                            if (isDarkTheme) Color(0xF1628FDE) else Color(0xF1114195)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val df = DecimalFormat("#.##")
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val km = 0.00075f * steps
                            val formatted = df.format(km)
                            Text(formatted.toString(), color = color, fontSize = 30.sp)
                            Text(
                                "Km",
                                fontSize = 20.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val kcal = 0.045f * steps
                            val formatted = df.format(kcal)
                            Text(formatted.toString(), color = color, fontSize = 30.sp)
                            Text(
                                "kcal",
                                fontSize = 20.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val x = (elapsedTime / 1000) / 60
                            Text(text = "$x", color = color, fontSize = 30.sp)
                            Text("Time", fontSize = 20.sp)
                        }
                    }
                }
                item {
                    Column {
//                        CustomizedButton(
//                            text = "Daily History", onClick = {
//                                navController.navigate(Screens.DailyHistory.name)
//                            },
//                            modifier = Modifier.padding(
//                                top = 26.dp, start = 16.dp,
//                                end = 16.dp
//                            )
//                        )
                        CustomizedButton(
                            text = "Weekly History", onClick = {
                                appViewModel.getAllStepsFromLocalDb()
                                navController.navigate(Screens.WeeklyHistory.name)
                            },
                            modifier = Modifier.padding(
                                top = 16.dp, start = 16.dp,
                                end = 16.dp
                            ),
                            isDarkTheme=isDarkTheme
                        )
                        CustomizedButton(
                            text = "Activity", onClick = {
                                navController.navigate(Screens.Activity.name)
                            },
                            iconColor = Color.Green,
                            icon = Icons.AutoMirrored.Filled.DirectionsRun,
                            modifier = Modifier.padding(
                                top = 16.dp, start = 16.dp,
                                end = 16.dp
                            ),
                            isDarkTheme = isDarkTheme
                        )

                    }
                }
            }
        }
    }
}
