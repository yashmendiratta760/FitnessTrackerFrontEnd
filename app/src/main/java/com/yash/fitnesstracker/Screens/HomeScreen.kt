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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.Login_Signup.Screens.Components.DialogueBoxOpt
import com.yash.fitnesstracker.Login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.Screens.Components.CircularProgress
import com.yash.fitnesstracker.Service. ForegroundService
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

    LaunchedEffect(steps)
    {
        appViewModel.insertOrUpdateInRoom(steps)
    }


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
                    navController.navigate("login"){
                        popUpTo(0){inclusive=true}
                    }
                }
            })
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgress(progress = progress)
                    Text(text = steps.toString(), fontSize = 50.sp)

                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val df = DecimalFormat("#.##")
                    Column {
                        val km = 0.00072f*steps
                        val formatted = df.format(km)
                        Text(formatted.toString(), color = Color.Green, fontSize = 30.sp)
                        Text("km", color = Color.Blue,fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Column {
                        val kcal = 0.035f*steps
                        val formatted = df.format(kcal)
                        Text(formatted.toString(),color = Color.Green, fontSize = 30.sp)
                        Text("kcal", color = Color.Blue, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Text("Activity Time")
                }
            }
            item {
                Column {
                    Text("Daily history")
                    Text("Weekly history")
                    Text("monthly history")
                }
            }
        }
    }
}