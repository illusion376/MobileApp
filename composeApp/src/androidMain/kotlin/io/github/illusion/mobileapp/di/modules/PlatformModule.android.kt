package io.github.illusion.mobileapp.di.modules

import eu.anifantakis.lib.ksafe.KSafe
import io.github.illusion.mobileapp.domain.haptic.AndroidHapticFeedback
import io.github.illusion.mobileapp.domain.haptic.HapticFeedback
import io.github.illusion.mobileapp.domain.health.AndroidStepCounter
import io.github.illusion.mobileapp.domain.health.StepCounter
import io.github.illusion.mobileapp.domain.location.AndroidLocationProvider
import io.github.illusion.mobileapp.domain.location.LocationProvider
import io.github.illusion.mobileapp.service.AndroidServiceStarter
import io.github.illusion.mobileapp.service.ServiceStarter
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformModule = module {
    single(named("vault")) {
        KSafe(
            context = androidApplication(),
            fileName = "vault"
        )
    }
    single<StepCounter> {
        AndroidStepCounter(context = androidContext())
    }
    single<ServiceStarter> {
        AndroidServiceStarter(context = androidContext())
    }
    single<LocationProvider> {
        AndroidLocationProvider(context = androidContext())
    }
    single<HapticFeedback> {
        AndroidHapticFeedback(context = androidContext())
    }
}