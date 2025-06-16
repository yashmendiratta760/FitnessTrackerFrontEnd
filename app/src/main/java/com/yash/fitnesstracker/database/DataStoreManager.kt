package com.yash.fitnesstracker.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object DataStoreManager {
    private val Context.dataStore by preferencesDataStore(name = "step_data")

    private val MIDNIGHT_BASE = intPreferencesKey("midnight_base")

    suspend fun saveMidnightBase(context: Context, steps: Int) {
        context.dataStore.edit { prefs ->
            prefs[MIDNIGHT_BASE] = steps
        }
    }

    suspend fun getMidnightBase(context: Context): Int {
        val prefs = context.dataStore.data.first()
        return prefs[MIDNIGHT_BASE] ?: 0
    }
}
