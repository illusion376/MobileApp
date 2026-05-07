package io.github.illusion.mobileapp.di.modules

import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.illusion.mobileapp.data.remote.api.UserApi
import io.github.illusion.mobileapp.data.repository.AuthRepositoryImpl
import io.github.illusion.mobileapp.domain.repository.AuthRepository
import io.github.illusion.mobileapp.domain.usecase.LoginUserUseCase
import io.github.illusion.mobileapp.domain.usecase.RegisterUserUseCase
import io.github.illusion.mobileapp.domain.usecase.VerificationUserUseCase
import io.github.illusion.mobileapp.ui.screens.login.LoginViewModel
import io.github.illusion.mobileapp.ui.screens.register.RegisterViewModel
import io.github.illusion.mobileapp.ui.screens.verification.VerificationViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val screenModule = module {
    singleOf(::UserApi)
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    factoryOf(::LoginUserUseCase)
    factoryOf(::RegisterUserUseCase)
    factoryOf(::VerificationUserUseCase)

    factory {
        LoginViewModel(get())
    }

    factory {
        RegisterViewModel(get())
    }

    factory {
        VerificationViewModel(get())
    }
}