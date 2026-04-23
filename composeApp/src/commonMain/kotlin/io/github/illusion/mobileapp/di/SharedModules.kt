package io.github.illusion.mobileapp.di

import io.github.illusion.mobileapp.di.modules.networkModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedModules = module {
    includes(networkModule)
    // Тут следует подключать сингтоны
}
