// composeApp/src/androidMain/kotlin/io/github/illusion/mobileapp/domain/health/StepCounter.android.kt
package io.github.illusion.mobileapp.domain.health

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidStepCounter(
    private val context: Context,
) : StepCounter {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private var listener: SensorEventListener? = null
    private var baselineSteps = -1
    private var lastEmittedSteps = 0

    override fun start(): Flow<Int> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (sensor == null) {
            trySend(0)
            close()
            return@callbackFlow
        }

        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val total = event.values[0].toInt()
                if (baselineSteps == -1) baselineSteps = total
                val steps = total - baselineSteps
                if (steps != lastEmittedSteps) {
                    lastEmittedSteps = steps
                    trySend(steps)
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        sensorManager.registerListener(
            listener,
            sensor,
            SensorManager.SENSOR_DELAY_FASTEST,
        )

        awaitClose {
            listener?.let { sensorManager.unregisterListener(it) }
            listener = null
        }
    }

    override fun stop() {
        listener?.let { sensorManager.unregisterListener(it) }
        listener = null
        baselineSteps = -1
        lastEmittedSteps = 0
    }
}