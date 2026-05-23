package io.github.illusion.mobileapp.di.modules

import eu.anifantakis.lib.ksafe.KSafe
import org.koin.android.ext.koin.androidApplication
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
}