package com.yash.fitnesstracker.Service

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yash.fitnesstracker.screens.components.pre
import kotlinx.coroutines.flow.first

object DataStoreManager {
    private val Context.dataStore by preferencesDataStore(name = "step_data")

    private val MIDNIGHT_BASE = intPreferencesKey("midnight_base")
    private val USER_NAME = stringPreferencesKey("user_name")
    private val TIME = longPreferencesKey("time")

    suspend fun saveMidnightBase(context: Context, steps: Int) {
        context.dataStore.edit { prefs ->
            prefs[MIDNIGHT_BASE] = steps
        }
    }

    suspend fun getMidnightBase(context: Context): Int {
        val prefs = context.dataStore.data.first()
        return prefs[MIDNIGHT_BASE] ?: 0
    }

    suspend fun saveTime(context: Context, time: Long) {
        context.dataStore.edit { prefs ->
            prefs[TIME] = time
        }
    }

    suspend fun getTime(context: Context): Long {
        val prefs = context.dataStore.data.first()
        return prefs[TIME] ?: 0L
    }

    suspend fun saveUserName(context: Context,name: String)
    {
        context.dataStore.edit { prefs->
            prefs[USER_NAME] = name
        }
    }

    suspend fun getUserName(context: Context): String
    {
        val prefs = context.dataStore.data.first()
        return prefs[USER_NAME] ?: ""
    }


}