package com.yash.fitnesstracker.Login_Signup.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.R

@Composable
fun OpenPage(navController: NavHostController)
{
    Column(modifier = Modifier.fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)) {
        val bgColor = MaterialTheme.colorScheme.background
        Log.d("BackgroundColor", "Background color: $bgColor")
        Image(
            painter = painterResource(R.drawable.main_page_removebg_preview),
            contentDescription = "Chat Image",
            modifier = Modifier.fillMaxWidth()
                .size(400.dp)
        )
        Spacer(modifier = Modifier.padding(10.dp))


        Button(onClick = {navController.navigate("Signup")},
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(start = 45.dp, end = 45.dp)) {
            Text("Signup", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
            Text("Already a user?  ")
            Text("Login",
                color = Color.Blue,modifier = Modifier.clickable(onClick = {  navController.navigate("login")}))
        }
    }
}

//@Preview(showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun pre()
//{
//    Login_SignupTheme {
//        OpenPage()
//
//    }
//}