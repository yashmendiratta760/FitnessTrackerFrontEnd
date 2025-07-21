package com.yash.fitnesstracker.login_Signup.repository

import com.yash.fitnesstracker.login_Signup.api.LoginSignupAPI
import com.yash.fitnesstracker.login_Signup.data.JwtResponse
import com.yash.fitnesstracker.login_Signup.data.LoginDTO
import com.yash.fitnesstracker.login_Signup.data.OtpValidateData
import com.yash.fitnesstracker.login_Signup.data.UserDTO
import retrofit2.Response

interface LoginSignupRepository
{
    suspend fun generateOtp(user: UserDTO):Response<String>
    suspend fun signup(validate: OtpValidateData):Response<Unit>
    suspend fun login(loginDTO: LoginDTO): Response<JwtResponse>
    suspend fun getEmail(loginDTO: LoginDTO):Response<String>
}

class LoginSignupRepositoryImpl(private val api: LoginSignupAPI):LoginSignupRepository
{
    override suspend fun generateOtp(user: UserDTO): Response<String> {
        return api.generateOtp(user)
    }

    override suspend fun signup(validate: OtpValidateData): Response<Unit> {
        return api.signup(validate)
    }

    override suspend fun login(loginDTO: LoginDTO): Response<JwtResponse> {
        return api.login(loginDTO)
    }

    override suspend fun getEmail(loginDTO: LoginDTO): Response<String> {
        return api.getEmail(loginDTO)
    }


}