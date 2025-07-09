package com.yash.fitnesstracker.repository

import com.yash.fitnesstracker.database.ActivityDAO
import com.yash.fitnesstracker.database.ActivityDTO
import com.yash.fitnesstracker.database.ActivityHistoryEntities

interface ActivityHistoryRepo
{
    suspend fun getData():List<ActivityDTO>
    suspend fun insert(activityHistoryEntities: ActivityHistoryEntities)
}

class OfflineActivityHistoryRepo(private val activityDAO: ActivityDAO) : ActivityHistoryRepo
{
    override suspend fun getData(): List<ActivityDTO> {
        return activityDAO.getData()
    }

    override suspend fun insert(activityHistoryEntities: ActivityHistoryEntities) {
        return activityDAO.insert(activityHistoryEntities)
    }

}
