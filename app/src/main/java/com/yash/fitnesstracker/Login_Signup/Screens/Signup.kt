package com.yash.fitnesstracker.Login_Signup.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.Login_Signup.Screens.Components.TextBox
import com.yash.fitnesstracker.Login_Signup.data.userDTO
import com.yash.fitnesstracker.Login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.R

@Composable
fun SignUp(
    navController:NavHostController,
    LoginSignupViewModel: LoginSignupViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

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
                value = email,
                onValueChange = { email = it },
                label = "Email",
                keyboardOption = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon =
                    {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "name"
                        )
                    })

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
                    if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        LoginSignupViewModel.uiState.value.name = name
                        val user = userDTO(name, email, password)
                        LoginSignupViewModel.generateOtp(user, context)
                        navController.navigate("otp")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
            ) {
                Text(text = "Send OTP")
            }

            Spacer(modifier = Modifier.padding(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Already a user?  ")
                Text(
                    "Login",
                    color = Color.Blue,
                    modifier = Modifier.clickable(onClick = { navController.navigate("login") })
                )
            }
        }
    }
}
