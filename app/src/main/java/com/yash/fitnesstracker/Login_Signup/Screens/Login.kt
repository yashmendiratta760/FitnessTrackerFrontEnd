package com.yash.fitnesstracker.Login_Signup.Screens

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import com.yash.fitnesstracker.navigation.Screens
import com.yash.fitnesstracker.ui.theme.test
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

                navController.navigate(Screens.Home.name) {
                    popUpTo(0) { inclusive = true }
                }
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Incorrect Credentials", Toast.LENGTH_SHORT).show()
            }
        }
        LoginSignupViewModel.resetLoginAttepmted(context)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(
                R.drawable._768682f_bd54_4591_8261_630b8e6c352a
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
                .statusBarsPadding()
                .padding(bottom = 250.dp)
                .height(550.dp),
            contentScale = ContentScale.Crop
        )
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight() // fills entire height
                .padding(top = 250.dp) // optional
        ) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, 0f)

                // First curve - top-left "J" shape
                cubicTo(
                    width * 0f, 0f,
                    width * 0f, height * 0.25f,
                    width * 0.25f, height * 0.25f
                )

                // Straight horizontal line
                lineTo(width * 0.7f, height * 0.25f)

                // Second curve - top-right downward curve
                cubicTo(
                    width * 0.95f, height * 0.25f,
                    width * 0.95f, height * 0.4f,
                    width, height * 0.5f
                )

                // Extend path to bottom of screen
                lineTo(width, height)
                lineTo(0f, height)

                // Close the shape
                close()
            }

            drawPath(
                path = path,
                color = test,
                style = Fill
            )
        }
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val dynamicBottomPadding = (screenHeight * 0.15).dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = dynamicBottomPadding,
                    end = 15.dp, start = 15.dp
                )
        ) {

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
                        if (name.isEmpty() || password.isEmpty()) {
                            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                        } else {
                            LoginSignupViewModel.uiState.value.name = name
                            val user = LoginDTO(name, password)
                            LoginSignupViewModel.Login(context, user)


                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                ) {
                    Text(text = "Login")
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Not a user?  ")
                    Text(
                        "Signup",
                        color = Color.Black,
                        modifier = Modifier.clickable(onClick = { navController.navigate(Screens.Signup.name){
                        popUpTo(0)
                            {
                                inclusive=true
                            }
                        } })
                    )
                }
            }
        }
    }
}


