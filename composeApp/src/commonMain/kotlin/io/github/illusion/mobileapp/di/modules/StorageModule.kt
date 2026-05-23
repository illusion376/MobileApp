package io.github.illusion.mobileapp.di.modules

import com.russhwolf.settings.Settings
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import io.github.illusion.mobileapp.di.SafeStorage
import io.github.illusion.mobileapp.domain.features.auth.TokenStorage
import io.github.illusion.mobileapp.domain.features.auth.TokenStorageImpl
import org.koin.dsl.module

val storageModule = module {
    single<Settings> { Settings() }

    single<TokenStorage> {
        TokenStorageImpl(get())
    }

    single { SafeStorage(get()) }
}