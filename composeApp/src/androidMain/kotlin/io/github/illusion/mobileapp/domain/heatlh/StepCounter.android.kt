package io.github.illusion.mobileapp.domain.health

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.util.Log
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
    private var useDetector = false
    private var detectorCount = 0

    override fun start(): Flow<Int> = callbackFlow {
        val counterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        val detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        val sensor = counterSensor ?: detectorSensor
        useDetector = counterSensor == null && detectorSensor != null

        if (sensor == null) {
            Log.w("StepCounter", "No step sensor available on this device")
            trySend(0)
            close()
            return@callbackFlow
        }

        Log.d("StepCounter", "Using sensor: ${sensor.name} (detector=$useDetector)")

        if (useDetector) detectorCount = 0

        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (useDetector) {
                    detectorCount++
                    trySend(detectorCount)
                } else {
                    val total = event.values[0].toInt()
                    if (baselineSteps == -1) baselineSteps = total
                    val steps = total - baselineSteps
                    if (steps != lastEmittedSteps) {
                        lastEmittedSteps = steps
                        trySend(steps)
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        val mainHandler = Handler(Looper.getMainLooper())
        sensorManager.registerListener(
            listener,
            sensor,
            SensorManager.SENSOR_DELAY_FASTEST,
            mainHandler,
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
        detectorCount = 0
    }
}