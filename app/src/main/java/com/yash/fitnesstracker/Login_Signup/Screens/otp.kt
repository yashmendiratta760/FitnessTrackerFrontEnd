package com.yash.fitnesstracker.Login_Signup.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.Login_Signup.Screens.Components.OtpInput
import com.yash.fitnesstracker.Login_Signup.data.otpValidateData
import com.yash.fitnesstracker.Login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.R

@Composable
fun Otp(LoginSignupViewModel: LoginSignupViewModel,
        navController: NavHostController) {

    val uiState by LoginSignupViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var otpe by remember { mutableStateOf("") }

    Column( modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
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

            OtpInput { otp ->
                otpe = otp
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val data = otpValidateData(email = uiState.email, otpe)
                    LoginSignupViewModel.Signup(data)
                    if (uiState.validation_status == 400) {
                        Toast.makeText(context, "Enter correct otp", Toast.LENGTH_SHORT).show()
                    } else {
                        navController.navigate("home"){
                            popUpTo(0){inclusive=true}
                        }
                        Toast.makeText(context, "Signup Succesfull", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Signup")
            }
        }
    }
}

