package com.yash.fitnesstracker.API

import com.yash.fitnesstracker.database.StepsDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ServerDbApi
{
    @POST("steps/create-entry")
    suspend fun sendSteps(@Body steps: StepsDTO):Response<Unit>

    @GET("steps/getAll")
    suspend fun getAllData(): Response<List<StepsDTO>>
}