package com.yash.fitnesstracker.repository

import com.yash.fitnesstracker.API.ServerDbApi
import com.yash.fitnesstracker.database.StepsDTO
import retrofit2.Response

interface ServerDbRepo
{
    suspend fun sendSteps(steps: StepsDTO): Response<Unit>
}

class OnlineServerDbRep(private val serverDbApi: ServerDbApi): ServerDbRepo
{
    override suspend fun sendSteps(steps: StepsDTO): Response<Unit> {
        return serverDbApi.sendSteps(steps);
    }

}
