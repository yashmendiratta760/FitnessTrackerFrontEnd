package com.yash.fitnesstracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface StepsDAO
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stepsEntities: StepsEntities)

    @Update
    suspend fun update(stepsEntities: StepsEntities)

    @Query("DELETE From Steps_track where id= :id")
    suspend fun deleteById(id:Int)

    @Query("SELECT * FROM Steps_track WHERE id = :id")
    fun getDataById(id: Int): Flow<StepsEntities>

    @Query("SELECT * FROM Steps_track WHERE date<:date Order by date ASC")
    fun fetchByDate(date: String): Flow<List<StepsEntities>>

    @Query("SELECT * FROM Steps_track WHERE date = :date LIMIT 1")
    suspend fun getStepsByDate(date: String): StepsEntities?
}