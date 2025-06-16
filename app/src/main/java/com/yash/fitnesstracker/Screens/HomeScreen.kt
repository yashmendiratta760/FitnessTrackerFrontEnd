package com.yash.fitnesstracker.Screens

import android.R.attr.shape
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yash.fitnesstracker.Login_Signup.Screens.Components.DialogueBoxOpt
import com.yash.fitnesstracker.Login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.Screens.Components.CircularProgress
import com.yash.fitnesstracker.Screens.Components.CustomizedButton
import com.yash.fitnesstracker.Service. ForegroundService
import com.yash.fitnesstracker.navigation.Screens
import com.yash.fitnesstracker.viewmodel.appViewModel
import java.text.DecimalFormat

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(modifier: Modifier= Modifier,
               appViewModel: appViewModel,
               loginSignupViewModel: LoginSignupViewModel,
               navController: NavHostController)
{

    val context = LocalContext.current

    var showDetail = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val intent = Intent(context, ForegroundService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    val steps = appViewModel.uiState.collectAsState().value.steps
    Log.d("UI", "Steps from ViewModel: $steps")
    val progress = steps / 10000f
    val time by remember { mutableStateOf(0)}


    Column {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, end = 30.dp),horizontalArrangement = Arrangement.End) {
            Box(modifier=modifier
                .size(45.dp)
                .clip(
                    RoundedCornerShape(30.dp)
                )
                .background(color = Color.Black)
                .clickable(onClick = { showDetail.value = true }),
                contentAlignment = Alignment.Center
            ){
                val name = loginSignupViewModel.uiState.collectAsState().value.name
                val firstLetter = name.firstOrNull { it.isLetter() }?.uppercaseChar()?.toString() ?: ""
                Text(text = firstLetter,
                    fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    color = Color.White)
            }
        }
        if (showDetail.value) {
            DialogueBoxOpt(showDetail,
                onLogoutClick = {
                    showDetail.value=false
                    loginSignupViewModel.logout(context){
                    navController.navigate(Screens.Login.name){
                        popUpTo(0){inclusive=true}
                    }
                }
            })
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
                        CircularProgress(progress = progress, modifier = Modifier.fillMaxSize())
                        Text(text = steps.toString(), fontSize = 40.sp)
                    }

                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val df = DecimalFormat("#.##")
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val km = 0.00075f*steps
                        val formatted = df.format(km)
                        Text(formatted.toString(), color = Color(0xFF0090F6), fontSize = 30.sp)
                        Text("Km", color = Color.White,
                             fontSize = 20.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val kcal = 0.045f*steps
                        val formatted = df.format(kcal)
                        Text(formatted.toString(),color = Color(0xFF0090F6), fontSize = 30.sp)
                        Text("kcal", color = Color.White,
                             fontSize = 20.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("20",color = Color(0xFF0090F6), fontSize = 30.sp)
                        Text("Time", color = Color.White, fontSize = 20.sp)
                    }
                }
            }
            item {
                Column {
                    CustomizedButton(text = "Daily History", onClick = {
                        navController.navigate(Screens.DailyHistory.name)
                    },
                        modifier = Modifier.padding(top = 26.dp, start = 16.dp,
                            end = 16.dp))
                    CustomizedButton(text = "Weekly History", onClick = {
                        navController.navigate(Screens.WeeklyHistory.name)
                    },
                        modifier = Modifier.padding(top = 16.dp,start = 16.dp,
                            end = 16.dp))
                    CustomizedButton(text = "Monthly History", onClick = {
                        navController.navigate(Screens.MonthlyHistory.name)
                    },
                        modifier = Modifier.padding(top = 16.dp,start = 16.dp,
                            end = 16.dp))

                }
            }
        }
    }
}
