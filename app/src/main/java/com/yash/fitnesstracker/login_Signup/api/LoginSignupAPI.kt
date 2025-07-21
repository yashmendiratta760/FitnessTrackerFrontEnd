package com.yash.fitnesstracker.login_Signup.api

import com.yash.fitnesstracker.login_Signup.data.JwtResponse
import com.yash.fitnesstracker.login_Signup.data.LoginDTO
import com.yash.fitnesstracker.login_Signup.data.OtpValidateData
import com.yash.fitnesstracker.login_Signup.data.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginSignupAPI
{
    @POST("users/generate-otp")
    suspend fun generateOtp(@Body user: UserDTO):Response<String>

    @POST("users/signup")
    suspend fun signup(@Body validate: OtpValidateData):Response<Unit>

    @POST("users/login")
    suspend fun login(@Body loginDTO: LoginDTO): Response<JwtResponse>

    @POST("users/getEmail")
    suspend fun getEmail(@Body loginDTO: LoginDTO):Response<String>
}