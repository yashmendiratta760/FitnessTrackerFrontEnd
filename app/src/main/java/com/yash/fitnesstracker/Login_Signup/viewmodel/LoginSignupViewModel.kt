package com.yash.fitnesstracker.Login_Signup.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yash.fitnesstracker.MainApplication
import com.yash.fitnesstracker.Login_Signup.repository.LoginSignupRepositoryImpl
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.yash.fitnesstracker.Login_Signup.data.LoginDTO
import com.yash.fitnesstracker.Login_Signup.data.otpValidateData
import com.yash.fitnesstracker.Login_Signup.data.userDTO
import com.yash.fitnesstracker.repository.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginSignupViewModel(private val repository: LoginSignupRepositoryImpl) : ViewModel()
{
    private val _uiState = MutableStateFlow(LoginSignupUiState())
    val uiState:StateFlow<LoginSignupUiState> = _uiState.asStateFlow()

    fun generateOtp(userDto: userDTO, context: Context)
    {
        viewModelScope.launch {
            try {
                val otp = repository.generateOtp(userDto)
                if(otp.code()==409)
                {
                    Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show()
                    (context as? Activity)?.finish()
                }
                _uiState.update { LoginSignupUiState -> LoginSignupUiState.copy(email = userDto.email,
                    otp=otp.body()?:"") }
                Log.d("successful ","otp generated")
            } catch (e: Exception) {
                Log.e("Error in generating Otp : ",e.toString())
            }

        }
    }

    fun Signup(validate: otpValidateData)
    {

        viewModelScope.launch {
            try {
                val response = repository.signup(validate)
                if(response.code()==400)
                {
                    _uiState.update { LoginSignupUiState-> LoginSignupUiState.copy(validation_status = 400) }
                    Log.e("otp","otp not validated")
                }
                Log.d("Signup","validation successfull")
            } catch (e: Exception) {
                Log.e("Error in signup : ",e.toString())
            }
        }

    }

    fun Login(context: Context,loginDTO: LoginDTO)
    {
        viewModelScope.launch {

            try {
                val response = repository.login(loginDTO)
                if(response.isSuccessful)
                {
                    val jwtToken = response.body()?.token
                    Log.d("jwt token", jwtToken?: "not found")
                    jwtToken?.let {
                        TokenManager.saveToken(context,it)
                    }

                    _uiState.value = _uiState.value.copy(isLoggedin = true,
                        loginAttempted = true)


                }
                else{
                    Log.e("Login","Login failed")

                    _uiState.value = _uiState.value.copy(isLoggedin = false,
                        loginAttempted = true)

                }
            }catch (e: Exception){
                Log.e("Error",e.toString())

                _uiState.value = _uiState.value.copy(isLoggedin = false,
                    loginAttempted = true)
            }
        }
    }

    fun logout(context: Context,onLogoutComplete: () -> Unit)
    {
        viewModelScope.launch {
            Log.d("Logout", "Token before clearing: ${TokenManager.getToken(context)}")
            TokenManager.clearToken(context)
            Log.d("Logout", "Token after clearing: ${TokenManager.getToken(context)}")
            _uiState.update { it.copy(isLoggedin = false) }
            onLogoutComplete()

        }
    }

    fun resetLoginAttepmted(context: Context)
    {
        _uiState.value = _uiState.value.copy(loginAttempted = false)
    }


    companion object
    {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =(this[APPLICATION_KEY] as MainApplication)
                val LoginSignupRepository = application.container.loginSignupRepository
                LoginSignupViewModel(LoginSignupRepository)
            }
        }
    }
}

