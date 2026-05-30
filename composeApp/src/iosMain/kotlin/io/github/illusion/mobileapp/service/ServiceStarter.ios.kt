package io.github.illusion.mobileapp.service

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.illusion.mobileapp.domain.health.StepCounter
import org.koin.compose.koinInject

@Composable
actual fun rememberServiceStarter(): (String) -> Unit {
    val stepCounter = koinInject<StepCounter>()

    val iosService = remember { IosTrainingService(stepCounter) }

    return remember {
        { action ->
            iosService.handleAction(action)
        }
    }
}