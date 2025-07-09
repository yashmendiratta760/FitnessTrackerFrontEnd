package com.yash.fitnesstracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Steps_track")
data class StepsEntities(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val steps:Int = 0,
    val date: String,
    val synced: Boolean = false
)