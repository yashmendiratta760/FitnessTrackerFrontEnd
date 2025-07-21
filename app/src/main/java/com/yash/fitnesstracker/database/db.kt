package com.yash.fitnesstracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StepsEntities::class], version = 2, exportSchema = false)
abstract class StepsDatabase(): RoomDatabase()
{
    abstract fun stepsDao(): StepsDAO

    companion object{

        @Volatile
        private var Instance: StepsDatabase?=null

        fun getDatabase(context: Context): StepsDatabase
        {
            return Instance?:synchronized(this){
                Room.databaseBuilder(context, StepsDatabase::class.java,
                    "Steps_db")
                    .build()
                    .also {Instance=it}
            }
        }
    }
}

@Database(entities = [ActivityHistoryEntities::class], version = 1, exportSchema = false)
abstract class ActivityDatabase(): RoomDatabase()
{
    abstract fun activityDao(): ActivityDAO

    companion object{

        @Volatile
        private var Instance: ActivityDatabase?=null

        fun getDatabase(context: Context): ActivityDatabase
        {
            return Instance?:synchronized(this){
                Room.databaseBuilder(context, ActivityDatabase::class.java,
                    "ActivityHistory_db")
                    .build()
                    .also {Instance=it}
            }
        }
    }
}