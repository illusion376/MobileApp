package io.github.illusion.mobileapp.service

object TrainingServiceBridge {
    var onStepsChanged: ((Int) -> Unit)? = null
    var onSecondsChanged: ((Int) -> Unit)? = null
}