package com.yash.fitnesstracker.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.yash.fitnesstracker.MainActivity
import com.yash.fitnesstracker.R
import com.yash.fitnesstracker.repository.StepRepository
import com.yash.fitnesstracker.utils.StopwatchHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForegroundService:Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var stepSensor: Sensor

    override fun onCreate() {
        super.onCreate()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!!

        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        Log.d("Sensor", "registered")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notification = NotificationCompat.Builder(this, "FitnessTrackerChannel")
            .setContentTitle("Steps Counter")
            .setContentText("Tracking your steps")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        Log.d("ForegroundService", "Foreground started")



        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "FitnessTrackerChannel",
                "Step Counter Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val stopwatch = StopwatchHelper()
    private var isStopwatchRunning = false
    private var lastStepTimestamp = System.currentTimeMillis()
    private val serviceScope = CoroutineScope(Dispatchers.Default)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_STEP_COUNTER) return

        val stepSinceLastReboot = event.values[0].toInt()

        CoroutineScope(Dispatchers.IO).launch {
            var midnightBase = DataStoreManager.getMidnightBase(applicationContext)
            if (midnightBase == 0) {
                midnightBase = stepSinceLastReboot
                DataStoreManager.saveMidnightBase(applicationContext, midnightBase)
            }


            val todaySteps = stepSinceLastReboot - midnightBase
            if(todaySteps<0)
            {
                DataStoreManager.saveMidnightBase(applicationContext,todaySteps)
            }

            StepRepository.updateSteps(todaySteps)
            StepRepository.insertOrUpdateInRoom(todaySteps)
        }

            if (!isStopwatchRunning) {
                serviceScope.launch {
                    val storedElapsed = DataStoreManager.getTime(applicationContext)

                    if (storedElapsed == 0L) {
                        stopwatch.reset()
                    } else {
                        stopwatch.setElapsedTime(storedElapsed)
                    }
                    stopwatch.start(serviceScope) { elapsed ->
                        StepRepository.updateTime(time = (elapsed / 1000) / 60)
                        serviceScope.launch {
                            DataStoreManager.saveTime(applicationContext, elapsed)
                        }
//                        val x = (elapsed / 1000) / 60
                    }
                    isStopwatchRunning = true
                }
            }

            lastStepTimestamp = System.currentTimeMillis()
            serviceScope.launch {
                delay(10_000)
                if (System.currentTimeMillis() - lastStepTimestamp >= 10_000 && isStopwatchRunning) {
                    stopwatch.stop()
                    isStopwatchRunning = false
                    Log.d("Stopwatch", "Paused due to inactivity")
                }
            }


        }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()

        // Unregister the sensor listener when service is destroyed
        sensorManager.unregisterListener(this)
    }

}
