package io.github.illusion.mobileapp.di.modules

import io.github.illusion.mobileapp.domain.features.auth.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        val tokenStorage: TokenStorage = get()

        HttpClient {
            install(SSE)
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }

            defaultRequest {
                tokenStorage.getToken()?.let { token ->
                    header("Authorization", "Bearer $token")
                }
            }
        }
    }
}