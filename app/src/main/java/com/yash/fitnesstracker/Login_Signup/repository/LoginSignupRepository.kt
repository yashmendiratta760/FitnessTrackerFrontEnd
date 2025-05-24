package com.yash.fitnesstracker.Login_Signup.repository

import com.yash.fitnesstracker.Login_Signup.API.LoginSignupAPI
import com.yash.fitnesstracker.Login_Signup.data.JwtResponse
import com.yash.fitnesstracker.Login_Signup.data.LoginDTO
import com.yash.fitnesstracker.Login_Signup.data.otpValidateData
import com.yash.fitnesstracker.Login_Signup.data.userDTO
import retrofit2.Response

interface LoginSignupRepository
{
    suspend fun generateOtp(user: userDTO):Response<String>
    suspend fun signup(validate: otpValidateData):Response<Unit>
    suspend fun login(loginDTO: LoginDTO): Response<JwtResponse>
}

class LoginSignupRepositoryImpl(private val api: LoginSignupAPI):LoginSignupRepository
{
    override suspend fun generateOtp(user: userDTO): Response<String> {
        return api.generateOtp(user)
    }

    override suspend fun signup(validate: otpValidateData): Response<Unit> {
        return api.signup(validate)
    }

    override suspend fun login(loginDTO: LoginDTO): Response<JwtResponse> {
        return api.login(loginDTO)
    }


}