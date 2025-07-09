package com.yash.fitnesstracker.service


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.yash.fitnesstracker.repository.StepRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SensorManagerHelper(private val context: Context): SensorEventListener
{
    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private var sensorManager: SensorManager?=null
    private var stepSensor:Sensor?=null

    fun startListening()
    {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        stepSensor?.let {
            sensorManager?.registerListener(this,it, SensorManager.SENSOR_DELAY_UI)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?)
    {
        if (event==null|| event.sensor.type != Sensor.TYPE_STEP_COUNTER) return

        val stepsSinceLastReboot = event.values[0].toInt()
        var midNightBase: Int
        serviceScope.launch {
            midNightBase = DataStoreManager.getMidnightBase(context)

            if (midNightBase == 0) {
                midNightBase = stepsSinceLastReboot
                DataStoreManager.saveMidnightBase(context, midNightBase)
            }

            val todaySteps = stepsSinceLastReboot - midNightBase
            StepRepository.updateSteps(todaySteps)

            StepRepository.insertOrUpdateInRoom(todaySteps)


        }

    }
//
//    fun stopListening() {
//
//        sensorManager?.unregisterListener(this)
//    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

}