package com.yash.fitnesstracker.login_Signup.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yash.fitnesstracker.MainApplication
import com.yash.fitnesstracker.login_Signup.repository.LoginSignupRepositoryImpl
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import com.yash.fitnesstracker.login_Signup.data.LoginDTO
import com.yash.fitnesstracker.login_Signup.data.OtpValidateData
import com.yash.fitnesstracker.login_Signup.data.UserDTO
import com.yash.fitnesstracker.service.DataStoreManager
import com.yash.fitnesstracker.repository.StepsLocalDbRepository
import com.yash.fitnesstracker.repository.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginSignupViewModel(private val repository: LoginSignupRepositoryImpl,
    private val stepsLocalDbRepository: StepsLocalDbRepository) : ViewModel()
{
    private val _uiState = MutableStateFlow(LoginSignupUiState())
    val uiState:StateFlow<LoginSignupUiState> = _uiState.asStateFlow()

    fun generateOtp(userDTO: UserDTO, context: Context)
    {
        viewModelScope.launch {
            try {
                val otp = repository.generateOtp(userDTO)
                if(otp.code()==409)
                {
                    Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show()
                }
                else {

                    DataStoreManager.saveUserName(context, userDTO.userName)

                    _uiState.update { loginSignupUiState ->
                        loginSignupUiState.copy(
                            email = userDTO.email,
                            otp = otp.body() ?: "",
                            isOtpGenerated = true
                        )
                    }
                    Log.d("successful ", "otp generated")
                }
            } catch (e: Exception) {
                Log.e("Error in generating Otp : ",e.toString())
            }

        }
    }

    fun resetValidationStatus()
    {
        _uiState.value.copy(validationStatus = 0)
    }

    fun resetOtpFlag() {
        _uiState.update { it.copy(isOtpGenerated = false) }
    }

    fun signup(validate: OtpValidateData,context: Context)
    {

        viewModelScope.launch {
            try {
                val response = repository.signup(validate)
                if(response.code()==400)
                {
                    _uiState.update { loginSignupUiState-> loginSignupUiState.copy(validationStatus = 400) }
                    Log.e("otp","otp not validated")
                }
                else if(response.code()==200)
                {
                    val jwtToken = response.body().toString()
                    jwtToken.let {
                        TokenManager.saveToken(context,it)
                    }

                    _uiState.value = _uiState.value.copy(isLoggedin = true,
                        loginAttempted = true,
                        validationStatus = 200)
                }
            } catch (e: Exception) {
                Log.e("Error in signup : ",e.toString())
            }
        }

    }

    fun login(context: Context,loginDTO: LoginDTO)
    {
        viewModelScope.launch {

            try {
                val email = repository.getEmail(loginDTO)
                val response = repository.login(loginDTO)
                if(response.isSuccessful)
                {
                    DataStoreManager.saveEmail(context, email = email.body()!!)
                    val jwtToken = response.body()?.token
                    Log.d("jwt token", jwtToken?: "not found")
                    jwtToken?.let {
                        TokenManager.saveToken(context,it)
                    }

                    _uiState.value = _uiState.value.copy(isLoggedin = true,
                        loginAttempted = true,
                        loginTriggered = true)


                }
                else{
                    Log.e("Login","Login failed")

                    _uiState.value = _uiState.value.copy(isLoggedin = false,
                        loginAttempted = true,
                        loginTriggered = true)

                }
            }catch (e: Exception){
                Log.e("Error",e.toString())

                _uiState.value = _uiState.value.copy(isLoggedin = false,
                    loginAttempted = true,
                    loginTriggered = true)
            }
        }
    }

    fun logout(context: Context,onLogoutComplete: () -> Unit)
    {
        viewModelScope.launch {

            DataStoreManager.clearAllData()
            stepsLocalDbRepository.clearAllData()
            Log.d("Logout", "Token before clearing: ${TokenManager.getToken(context)}")
            TokenManager.clearToken(context)
            Log.d("Logout", "Token after clearing: ${TokenManager.getToken(context)}")
            _uiState.update { it.copy(isLoggedin = false) }
            onLogoutComplete()

        }
    }

    fun resetLoginAttepmted()
    {
        _uiState.value = _uiState.value.copy(loginAttempted = false)
    }


    companion object
    {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =(this[APPLICATION_KEY] as MainApplication)
                val loginSignupRepository = application.container.loginSignupRepository
                val stepsLocalDbRepository = application.container.stepsLocalDbRepository
                LoginSignupViewModel(loginSignupRepository,stepsLocalDbRepository)
            }
        }
    }
}

