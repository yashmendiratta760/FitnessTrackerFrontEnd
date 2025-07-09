package com.yash.fitnesstracker.repository

import com.yash.fitnesstracker.api.ServerDbApi
import com.yash.fitnesstracker.api.UploadResponse
import com.yash.fitnesstracker.database.StepsDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Part

interface ServerDbRepo
{
    suspend fun sendSteps(steps: StepsDTO): Response<Unit>
    suspend fun getAllData(): Response<List<StepsDTO>>
    suspend fun uploadAndGetImage(@Part("file") file: MultipartBody.Part,
                                  @Part("userName") userName: RequestBody) : Response<UploadResponse>

    suspend fun getImageUrl(): Response<String>
}

class OnlineServerDbRep(private val serverDbApi: ServerDbApi): ServerDbRepo
{
    override suspend fun sendSteps(steps: StepsDTO): Response<Unit> {
        return serverDbApi.sendSteps(steps);
    }

    override suspend fun getAllData():  Response<List<StepsDTO>> {
        return serverDbApi.getAllData()
    }

    override suspend fun uploadAndGetImage(
        file: MultipartBody.Part,
        userName: RequestBody
    ): Response<UploadResponse> {
        return serverDbApi.uploadAndGetImage(file,userName)
    }

    override suspend fun getImageUrl(): Response<String> {
        return serverDbApi.getImageUrl()
    }


}
