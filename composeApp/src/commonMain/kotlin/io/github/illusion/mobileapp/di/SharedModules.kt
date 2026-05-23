package io.github.illusion.mobileapp.di

import io.github.illusion.mobileapp.di.modules.networkModule
import io.github.illusion.mobileapp.di.modules.platformModule
import io.github.illusion.mobileapp.di.modules.screenModule
import io.github.illusion.mobileapp.di.modules.storageModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedModules = module {
    includes(platformModule)
    includes(storageModule)
    includes(networkModule)
    includes(screenModule)
    // Тут следует подключать модули
}
