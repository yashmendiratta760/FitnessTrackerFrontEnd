package com.yash.fitnesstracker.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.Screens.Components.CustomisedTopBar
import com.yash.fitnesstracker.navigation.Screens

@Composable
fun DailyHistory(navController: NavHostController)
{
    Scaffold(
        topBar = {
            CustomisedTopBar("Daily History", navController = navController)
        }
    ) {innerPadding->
        Column(modifier = Modifier.fillMaxSize()
            .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text("Daily History", fontSize = 50.sp)
        }
    }

}