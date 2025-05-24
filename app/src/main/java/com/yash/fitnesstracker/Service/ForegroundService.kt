package com.yash.fitnesstracker.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.yash.fitnesstracker.R
import com.yash.fitnesstracker.repository.StepRepository
import com.yash.fitnesstracker.viewmodel.AppUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForegroundService:Service(), SensorEventListener
{

    private lateinit var sensorManager: SensorManager
    private lateinit var stepSensor: Sensor

    override fun onCreate() {
        super.onCreate()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!!

        if (stepSensor == null) {
            Log.e("StepCounterService", "Step Counter sensor not available on this device")
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            Log.d("Sensor","registered")
        }
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this,"FitnessTrackerChannel")
            .setContentTitle("Steps Counter")
            .setContentText("Tracking your steps")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(1,notification)
        Log.d("ForegroundService", "Foreground started")



        return START_STICKY
    }

    private fun createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(
                "FitnessTrackerChannel",
                "Step Counter Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private var initialStepCount: Int? = null

    override fun onSensorChanged(event: SensorEvent?) {
        if (event==null|| event.sensor.type != Sensor.TYPE_STEP_COUNTER) return

        val stepSinceLastReboot = event.values[0].toInt()

        if(initialStepCount==null)
        {
            initialStepCount=stepSinceLastReboot
        }
        val currentSteps = stepSinceLastReboot-initialStepCount!!



        StepRepository.updateSteps(currentSteps)

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unregister the sensor listener when service is destroyed
        sensorManager.unregisterListener(this)
    }

}