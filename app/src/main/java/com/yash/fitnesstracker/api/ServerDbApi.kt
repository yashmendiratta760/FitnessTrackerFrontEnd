package com.yash.fitnesstracker.api

import com.yash.fitnesstracker.database.StepsDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
data class UploadResponse(val imageUrl: String)
interface ServerDbApi
{
    @POST("steps/create-entry")
    suspend fun sendSteps(@Body steps: StepsDTO):Response<Unit>

    @GET("steps/getAll")
    suspend fun getAllData(): Response<List<StepsDTO>>

    @Multipart
    @POST("cloudinary/upload")
    suspend fun uploadAndGetImage(
        @Part file: MultipartBody.Part,
        @Part("userName") userName: RequestBody
    ): Response<UploadResponse>

    @GET("cloudinary/getImage")
    suspend fun getImageUrl(): Response<String>
}