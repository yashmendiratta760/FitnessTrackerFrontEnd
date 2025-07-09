package com.yash.fitnesstracker.repository

import android.content.Context
import com.yash.fitnesstracker.API.ServerDbApi
import com.yash.fitnesstracker.Login_Signup.API.LoginSignupAPI
import com.yash.fitnesstracker.Login_Signup.repository.LoginSignupRepositoryImpl
import com.yash.fitnesstracker.config.AuthInterceptor
import com.yash.fitnesstracker.database.ActivityDatabase
import com.yash.fitnesstracker.database.StepsDatabase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

interface AppContainer
{
    val stepsLocalDbRepository: StepsLocalDbRepository
    val loginSignupRepository: LoginSignupRepositoryImpl
    val onlineServerDbRep : OnlineServerDbRep
    val activityHistoryRepo: ActivityHistoryRepo
}
class DefaultAppContainer(private val context: Context): AppContainer
{
    override val stepsLocalDbRepository: StepsLocalDbRepository by lazy {
        OfflineStepsLocalDbRepository(StepsDatabase.getDatabase(context).stepsDao())
    }

    override val activityHistoryRepo: ActivityHistoryRepo by lazy {
        OfflineActivityHistoryRepo(ActivityDatabase.getDatabase(context).activityDao())
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(context))
        .build()


    val retrofit = Retrofit.Builder()
        .baseUrl("https://fitnesstracker-p8kx.onrender.com")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val retrofitservice = retrofit.create(LoginSignupAPI::class.java)

    val retrofitserviceServerDb = retrofit.create(ServerDbApi::class.java)

    override val loginSignupRepository: LoginSignupRepositoryImpl by lazy {
        LoginSignupRepositoryImpl(retrofitservice);
    }

    override val onlineServerDbRep: OnlineServerDbRep by lazy {
        OnlineServerDbRep(retrofitserviceServerDb)
    }


}