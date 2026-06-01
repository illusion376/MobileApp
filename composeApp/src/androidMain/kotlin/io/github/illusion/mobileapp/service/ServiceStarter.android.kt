package io.github.illusion.mobileapp.service

import android.content.Context
import android.content.Intent
import android.util.Log

class AndroidServiceStarter(private val context: Context) : ServiceStarter {
    override fun start(action: String) {
        try {
            val intent = Intent(context, TrainingService::class.java).apply {
                this.action = action
            }
            context.startForegroundService(intent)
        } catch (e: Exception) {
            Log.e("ServiceStarter", "Failed to start service: ${e.message}", e)
        }
    }
}
