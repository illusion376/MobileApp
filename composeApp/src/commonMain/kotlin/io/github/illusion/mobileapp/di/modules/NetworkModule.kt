package io.github.illusion.mobileapp.di.modules

import io.github.illusion.mobileapp.data.remote.api.UserApi
import io.github.illusion.mobileapp.data.repository.AuthRepositoryImpl
import io.github.illusion.mobileapp.domain.repository.AuthRepository
import io.github.illusion.mobileapp.domain.usecase.LoginUserUseCase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }
        }
    }
    singleOf(::UserApi)
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    factoryOf(::LoginUserUseCase)
}