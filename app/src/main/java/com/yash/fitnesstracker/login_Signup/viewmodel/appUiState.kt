package com.yash.fitnesstracker.login_Signup.viewmodel

data class LoginSignupUiState(
    var email:String = "",
    var otp:String = "",
    var validationStatus: Int = 0,
    var isLoggedin: Boolean = false,
    var loginAttempted: Boolean = false,
    var name:String="Fitness",
    val isOtpGenerated: Boolean = false,
    val loginTriggered: Boolean = false

)