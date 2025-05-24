package com.yash.fitnesstracker.Login_Signup.Screens

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.Login_Signup.Screens.Components.TextBox
import com.yash.fitnesstracker.Login_Signup.data.LoginDTO
import com.yash.fitnesstracker.Login_Signup.viewmodel.LoginSignupUiState
import com.yash.fitnesstracker.Login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.R
import kotlinx.coroutines.coroutineScope


@Composable
fun Login(
    navController:NavHostController,
    LoginSignupViewModel: LoginSignupViewModel,
    uiState: LoginSignupUiState
)
{

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val permissionsToRequest = mutableListOf<String>()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACTIVITY_RECOGNITION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        permissionsToRequest.add(android.Manifest.permission.ACTIVITY_RECOGNITION)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        permissionsToRequest.add(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    if (permissionsToRequest.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            context as Activity,
            permissionsToRequest.toTypedArray(),
            1001 // Single request code
        )
    }

    LaunchedEffect(uiState.loginAttempted){
        if (uiState.loginAttempted) {
            if (uiState.isLoggedin) {

                navController.navigate("home") {
                    popUpTo(0) { inclusive = true }
                }
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Incorrect Credentials", Toast.LENGTH_SHORT).show()
            }
        }
        LoginSignupViewModel.resetLoginAttepmted(context)
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),) {
        Image(
            painter = painterResource(R.drawable.main_page_removebg_preview),
            contentDescription = "Chat Image",
            modifier = Modifier.fillMaxWidth()
                .size(400.dp)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Column(

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            TextBox(
                value = name,
                onValueChange = { name = it },
                label = "Username",
                keyboardOption = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon =
                    {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "name"
                        )
                    }
            )

            Spacer(modifier = Modifier.padding(10.dp))


            TextBox(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                keyboardOption = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon =
                    {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "name"
                        )
                    })

            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                onClick = {
                    if (name.isEmpty()  || password.isEmpty()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        LoginSignupViewModel.uiState.value.name = name
                        val user = LoginDTO(name, password)
                        LoginSignupViewModel.Login(context,user)


                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.padding(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Not a user?  ")
                Text(
                    "Signup",
                    color = Color.Blue,
                    modifier = Modifier.clickable(onClick = { navController.navigate("Signup") })
                )
            }
        }
    }
}


