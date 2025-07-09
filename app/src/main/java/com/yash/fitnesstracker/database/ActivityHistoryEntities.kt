package com.yash.fitnesstracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Activity_History")
data class ActivityHistoryEntities(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val time: String,
    val cal: Double
)
