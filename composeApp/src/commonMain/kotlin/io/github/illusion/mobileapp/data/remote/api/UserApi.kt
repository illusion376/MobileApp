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
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserApi(private val httpClient: HttpClient){
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

    suspend fun verification(requestDTO: VerificationRequestDTO): VerificationResponseDTO {
        return httpClient.post("${HttpString.URL}/verefication") {
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }
}