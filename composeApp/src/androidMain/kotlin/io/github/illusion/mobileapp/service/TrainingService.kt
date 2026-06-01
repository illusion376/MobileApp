package io.github.illusion.mobileapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import io.github.illusion.mobileapp.MainActivity
import io.github.illusion.mobileapp.domain.health.StepCounter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TrainingService : Service() {

    private val stepCounter: StepCounter by inject()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var stepJob: Job? = null
    private var timerJob: Job? = null

    private var currentSteps = 0
    private var currentSeconds = 0
    private var stepsOffset = 0

    companion object {
        const val CHANNEL_ID = "training_channel"
        const val NOTIFICATION_ID = 1
        private const val TAG = "TrainingService"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            TrainingServiceActions.ACTION_START -> startTraining()
            TrainingServiceActions.ACTION_STOP -> stopTraining()
            TrainingServiceActions.ACTION_PAUSE -> pauseTraining()
            TrainingServiceActions.ACTION_RESUME -> resumeTraining()
        }
        return START_STICKY
    }

    private fun startTraining() {
        currentSteps = 0
        currentSeconds = 0
        stepsOffset = 0
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceCompat.startForeground(
                    this,
                    NOTIFICATION_ID,
                    buildNotification(0, 0),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE,
                )
            } else {
                startForeground(NOTIFICATION_ID, buildNotification(0, 0))
            }
        } catch (e: Exception) {
            Log.e(TAG, "startForeground failed: ${e.message}", e)
        }
        startStepCounting()
        startTimer()
    }

    private fun pauseTraining() {
        stepsOffset = currentSteps
        stepJob?.cancel()
        stepCounter.stop()
        timerJob?.cancel()
        updateNotification()
    }

    private fun resumeTraining() {
        startStepCounting()
        startTimer()
    }

    private fun stopTraining() {
        currentSteps = 0
        currentSeconds = 0
        stepsOffset = 0
        stepJob?.cancel()
        timerJob?.cancel()
        stepCounter.stop()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun startStepCounting() {
        stepJob?.cancel()
        stepJob = scope.launch {
            stepCounter.start().collect { newSteps ->
                currentSteps = stepsOffset + newSteps
                TrainingServiceBridge.onStepsChanged?.invoke(currentSteps)
                updateNotification()
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            while (true) {
                delay(1000)
                currentSeconds++
                TrainingServiceBridge.onSecondsChanged?.invoke(currentSeconds)
                updateNotification()
            }
        }
    }

    private fun updateNotification() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, buildNotification(currentSteps, currentSeconds))
    }

    private fun buildNotification(steps: Int, seconds: Int) =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Тренировка идёт...")
            .setContentText(
                "Шаги: $steps | Время: ${seconds / 60}:${(seconds % 60).toString().padStart(2, '0')}"
            )
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    this, 0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Тренировка",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        stepCounter.stop()
    }
}