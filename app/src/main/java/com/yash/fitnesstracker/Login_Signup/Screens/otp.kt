package com.yash.fitnesstracker.Login_Signup.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.yash.fitnesstracker.Login_Signup.Screens.Components.OtpInput
import com.yash.fitnesstracker.Login_Signup.data.otpValidateData
import com.yash.fitnesstracker.Login_Signup.viewmodel.LoginSignupViewModel
import com.yash.fitnesstracker.R
import com.yash.fitnesstracker.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Otp(LoginSignupViewModel: LoginSignupViewModel,
        navController: NavHostController) {

    val uiState by LoginSignupViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var otpe by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(start = 16.dp, end = 16.dp, top = 50.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.picture3),
                    contentDescription = "Verification Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(200.dp)
//                        .padding(96.dp)
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Assuming 'big', 'bold', 'medium', 'light' are custom TextStyles or modifiers.
            // Replace with appropriate MaterialTheme typography or define them if you haven't.
            Text(
                text = "Verification",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Otp has been sent to your email.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)

            )
            Spacer(modifier = Modifier.height(50.dp))

            Spacer(modifier = Modifier.height(10.dp))

            OtpInput { enteredOtp ->
                otpe = enteredOtp
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val data = otpValidateData(email = uiState.email, otp = otpe)
                        LoginSignupViewModel.Signup(data,context)




                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "Verify")
            }

            LaunchedEffect(uiState.validation_status) {
                when (uiState.validation_status) {
                    400 -> {
                        Toast.makeText(context, "Enter correct OTP", Toast.LENGTH_SHORT).show()
                        LoginSignupViewModel.resetValidationStatus()
                    }
                    200 -> {
                        Toast.makeText(context, "Signup successful.", Toast.LENGTH_SHORT).show()
                        LoginSignupViewModel.resetValidationStatus()
                        navController.navigate(Screens.Profile.name) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }

        }
    }
}

