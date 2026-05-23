package io.github.illusion.mobileapp.di.modules

import com.russhwolf.settings.Settings
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import io.github.illusion.mobileapp.data.repository.KSafeRepositoryImpl
import io.github.illusion.mobileapp.domain.features.auth.TokenStorage
import io.github.illusion.mobileapp.domain.features.auth.TokenStorageImpl
import io.github.illusion.mobileapp.domain.repository.KSafeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val storageModule = module {
    single<KSafeRepository> {
        KSafeRepositoryImpl(
            kSafe = get<KSafe>(named("vault"))
        )
    }
}