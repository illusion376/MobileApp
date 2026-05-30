package io.github.illusion.mobileapp.service

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberServiceStarter(): (String) -> Unit {
    val context = LocalContext.current
    return remember {
        { action ->
            val intent = Intent(context, TrainingService::class.java).apply {
                this.action = action
            }
            context.startForegroundService(intent)
        }
    }
}