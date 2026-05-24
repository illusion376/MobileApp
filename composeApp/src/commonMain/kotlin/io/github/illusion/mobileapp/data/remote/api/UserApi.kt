package io.github.illusion.mobileapp.data.remote.api

import io.github.illusion.mobileapp.data.remote.HttpString
import io.github.illusion.mobileapp.data.remote.dto.LoginRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.LoginResponseDTO
import io.github.illusion.mobileapp.data.remote.dto.RegisterRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.RegisterResponseDTO
import io.github.illusion.mobileapp.data.remote.dto.VerificationRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.VerificationResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class UserApi(private val httpClient: HttpClient) {
    suspend fun login(requestDTO: LoginRequestDTO): LoginResponseDTO {
        return httpClient.post("${HttpString.URL}/login") {
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    suspend fun register(requestDTO: RegisterRequestDTO): RegisterResponseDTO {
        return httpClient.post("${HttpString.URL}/register") {
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    fun verification(requestDTO: VerificationRequestDTO): Flow<Result<VerificationResponseDTO>> = callbackFlow {
        launch {
            try {
                httpClient.sse(
                    urlString = "${HttpString.URL}/auth/status/stream",
                    request = {
                        url {
                            parameters.append("email", requestDTO.email)
                        }
                    }
                ) {
                    incoming.collect { event ->
                        when (event.event) {
                            "verified" -> {
                                trySend(Result.success(VerificationResponseDTO(isVerified = true)))
                                close()
                                return@collect
                            }
                            "pending" -> {
                                trySend(Result.success(VerificationResponseDTO(isVerified = false)))
                            }
                            "error" -> {
                                trySend(Result.failure(Exception(event.data)))
                                close()
                                return@collect
                            }
                        }
                    }
                }
                close()
            } catch (e: Exception) {
                trySend(Result.failure(e))
                close()
            }
        }
        awaitClose()
    }
}