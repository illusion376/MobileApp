package io.github.illusion.mobileapp.service

import io.github.illusion.mobileapp.domain.health.StepCounter

class IosServiceStarter(stepCounter: StepCounter) : ServiceStarter {
    private val iosService = IosTrainingService(stepCounter)

    override fun start(action: String) {
        iosService.handleAction(action)
    }
}
