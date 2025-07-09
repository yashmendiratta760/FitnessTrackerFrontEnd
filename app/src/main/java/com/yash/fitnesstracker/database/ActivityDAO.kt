package com.yash.fitnesstracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ActivityDAO
{
    @Query("SELECT name , time, cal FROM Activity_History")
    suspend fun getData():List<ActivityDTO>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(activityEntities: ActivityHistoryEntities)
}
