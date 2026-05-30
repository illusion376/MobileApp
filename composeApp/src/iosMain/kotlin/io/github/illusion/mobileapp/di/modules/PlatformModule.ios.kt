package io.github.illusion.mobileapp.di.modules

import eu.anifantakis.lib.ksafe.KSafe
import io.github.illusion.mobileapp.domain.haptic.HapticFeedback
import io.github.illusion.mobileapp.domain.haptic.IosHapticFeedback
import io.github.illusion.mobileapp.domain.health.IosPedometerStepCounter
import io.github.illusion.mobileapp.domain.health.StepCounter
import io.github.illusion.mobileapp.domain.location.IosLocationProvider
import io.github.illusion.mobileapp.domain.location.LocationProvider
import io.github.illusion.mobileapp.service.IosServiceStarter
import io.github.illusion.mobileapp.service.ServiceStarter
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformModule = module {
    single(named("vault")) {
        KSafe(fileName = "vault")
    }
    single<StepCounter> { IosPedometerStepCounter() }
    single<ServiceStarter> { IosServiceStarter(get()) }
    single<LocationProvider> { IosLocationProvider() }
    single<HapticFeedback> { IosHapticFeedback() }
}