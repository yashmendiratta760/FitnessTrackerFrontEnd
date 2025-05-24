package com.yash.fitnesstracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StepsEntities::class], version = 1, exportSchema = false)
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