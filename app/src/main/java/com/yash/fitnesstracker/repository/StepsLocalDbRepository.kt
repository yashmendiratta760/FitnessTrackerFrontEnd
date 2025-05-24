package com.yash.fitnesstracker.repository

import android.R
import com.yash.fitnesstracker.database.StepsDAO
import com.yash.fitnesstracker.database.StepsEntities
import kotlinx.coroutines.flow.Flow

interface StepsLocalDbRepository
{
    suspend fun insert(stepsEntities: StepsEntities)
    suspend fun update(stepsEntities: StepsEntities)
    suspend fun getDataById(Id: Int): Flow<StepsEntities>
    suspend fun fetchByDate(date: String): Flow<List<StepsEntities>>
    suspend fun getStepsByDate(date: String): StepsEntities?


}

class OfflineStepsLocalDbRepository(private val stepsDAO: StepsDAO): StepsLocalDbRepository
{
    override suspend fun insert(stepsEntities: StepsEntities) {
        stepsDAO.insert(stepsEntities)
    }

    override suspend fun update(stepsEntities: StepsEntities) {
        stepsDAO.update(stepsEntities)
    }

    override suspend fun getDataById(Id: Int): Flow<StepsEntities> {
        return stepsDAO.getDataById(Id)
    }

    override suspend fun fetchByDate(date: String): Flow<List<StepsEntities>> {
        return stepsDAO.fetchByDate(date)
    }

    override suspend fun getStepsByDate(date: String): StepsEntities? {
        return stepsDAO.getStepsByDate(date)
    }


}