package io.github.illusion.mobileapp.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        modules(sharedModules)
    }
}