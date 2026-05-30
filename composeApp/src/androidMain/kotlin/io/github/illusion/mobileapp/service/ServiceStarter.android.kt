package io.github.illusion.mobileapp.service

import android.content.Context
import android.content.Intent

class AndroidServiceStarter(private val context: Context) : ServiceStarter {
    override fun start(action: String) {
        val intent = Intent(context, TrainingService::class.java).apply {
            this.action = action
        }
        context.startForegroundService(intent)
    }
}
