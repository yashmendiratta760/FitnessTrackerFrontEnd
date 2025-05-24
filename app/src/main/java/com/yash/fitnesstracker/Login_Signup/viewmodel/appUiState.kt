package com.yash.fitnesstracker.Login_Signup.viewmodel

data class LoginSignupUiState(
    var email:String = "",
    var otp:String = "",
    var validation_status: Int = 0,
    var isLoggedin: Boolean = false,
    var loginAttempted: Boolean = false,
    var name:String="Fitness"
)