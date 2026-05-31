package io.github.illusion.mobileapp.di.modules

import io.github.illusion.mobileapp.data.remote.api.PlayerApi
import io.github.illusion.mobileapp.data.remote.api.UserApi
import io.github.illusion.mobileapp.data.repository.AuthRepositoryImpl
import io.github.illusion.mobileapp.data.repository.PlayerRepositoryImpl
import io.github.illusion.mobileapp.data.repository.KSafeRepositoryImpl
import io.github.illusion.mobileapp.domain.repository.AuthRepository
import io.github.illusion.mobileapp.domain.repository.PlayerRepository
import io.github.illusion.mobileapp.domain.repository.KSafeRepository
import io.github.illusion.mobileapp.domain.usecase.GetCharacterUseCase
import io.github.illusion.mobileapp.domain.usecase.LoginUserUseCase
import io.github.illusion.mobileapp.domain.usecase.RegisterUserUseCase
import io.github.illusion.mobileapp.domain.usecase.VerificationUserUseCase
import io.github.illusion.mobileapp.ui.screens.login.LoginViewModel
import io.github.illusion.mobileapp.ui.screens.main.MainViewModel
import io.github.illusion.mobileapp.ui.screens.register.RegisterViewModel
import io.github.illusion.mobileapp.ui.screens.training.TrainingViewModel
import io.github.illusion.mobileapp.ui.screens.verification.VerificationViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val screenModule = module {
    singleOf(::UserApi)
    singleOf(::PlayerApi)

    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::PlayerRepositoryImpl) bind PlayerRepository::class

    factoryOf(::LoginUserUseCase)
    factoryOf(::RegisterUserUseCase)
    factoryOf(::VerificationUserUseCase)
    factoryOf(::GetCharacterUseCase)

    factory {
        LoginViewModel(get(), get())
    }

    factory {
        RegisterViewModel(get())
    }

    factory {
        VerificationViewModel(get())
    }

    factory {
        MainViewModel(get(), get())
    }

    factory {
        TrainingViewModel(get(), get(), playerRepository = get())
    }
}