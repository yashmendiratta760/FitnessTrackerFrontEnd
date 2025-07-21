package com.yash.fitnesstracker.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yash.fitnesstracker.viewmodel.UserDataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DataStoreManager {
    private val Context.dataStore by preferencesDataStore(name = "step_data")

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val MIDNIGHT_BASE = intPreferencesKey("midnight_base")
    private val USER_NAME = stringPreferencesKey("user_name")
    private val TIME = longPreferencesKey("time")
    private val NAME = stringPreferencesKey("name")
    private val AGE = intPreferencesKey("age")
    private val WEIGHT = doublePreferencesKey("weight")
    private val HEIGHT = doublePreferencesKey("height")
    private val STEPS_GOAL = intPreferencesKey("stepsGoal")
    private val LAST_SYNC_DATE = stringPreferencesKey("lastSyncDate")
    private val EMAIL_ID = stringPreferencesKey("email")

    suspend fun getLastSyncDate(): String? {
        val preferences = appContext.dataStore.data.first()
        return preferences[LAST_SYNC_DATE]
    }

    suspend fun setLastSyncDate(date: String) {
        appContext.dataStore.edit { prefs ->
            prefs[LAST_SYNC_DATE] = date
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isToday(date: String?): Boolean {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return date == today
    }


    suspend fun clearAllData()
    {
        appContext.dataStore.edit { prefs->
            prefs.clear()
        }
    }

    fun getUserData(): Flow<UserDataState>
    {
        return appContext.dataStore.data.map { prefs->
            UserDataState(
                name = prefs[NAME] ?: "",
                age = (prefs[AGE] ?: 0).toString(),
                weight = (prefs[WEIGHT] ?: 0.0).toString(),
                height = (prefs[HEIGHT] ?: 0.0).toString(),
                stepsGoal = (prefs[STEPS_GOAL] ?: 0).toString(),
                userName = (prefs[USER_NAME] ?:""),
                email = (prefs[EMAIL_ID] ?:"")
            )
        }
    }

    suspend fun saveEmail(context: Context,email:String)
    {
        context.dataStore.edit { prefs->
            prefs[EMAIL_ID] = email
        }
    }


    suspend fun saveSteps(context: Context, stepsGoal: Int) {
        context.dataStore.edit { prefs ->
            prefs[STEPS_GOAL] = stepsGoal
        }
    }

    suspend fun saveWeight(context: Context, weight: Double) {
        context.dataStore.edit { prefs ->
            prefs[WEIGHT] = weight
        }
    }

    suspend fun saveHeight(context: Context, height: Double) {
        context.dataStore.edit { prefs ->
            prefs[HEIGHT] = height
        }
    }

    suspend fun saveName(context: Context, name:String) {
        context.dataStore.edit { prefs ->
            prefs[NAME] = name
        }
    }

    suspend fun saveAge(context: Context, age: Int) {
        context.dataStore.edit { prefs ->
            prefs[AGE] = age
        }
    }

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


}