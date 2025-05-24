package com.yash.fitnesstracker.Login_Signup.API

import com.yash.fitnesstracker.Login_Signup.data.JwtResponse
import com.yash.fitnesstracker.Login_Signup.data.LoginDTO
import com.yash.fitnesstracker.Login_Signup.data.otpValidateData
import com.yash.fitnesstracker.Login_Signup.data.userDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginSignupAPI
{
    @POST("users/generate-otp")
    suspend fun generateOtp(@Body user: userDTO):Response<String>

    @POST("users/signup")
    suspend fun signup(@Body validate: otpValidateData):Response<Unit>

    @POST("users/login")
    suspend fun login(@Body loginDTO: LoginDTO): Response<JwtResponse>
}