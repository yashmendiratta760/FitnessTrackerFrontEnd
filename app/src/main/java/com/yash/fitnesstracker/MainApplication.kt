package com.yash.fitnesstracker

import android.app.Application
import com.yash.fitnesstracker.repository.AppContainer
import com.yash.fitnesstracker.repository.DefaultAppContainer

class MainApplication : Application()
{
    lateinit var  container: AppContainer
    override fun onCreate()
    {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}