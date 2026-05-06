package io.github.illusion.mobileapp.data.remote.api

import io.github.illusion.mobileapp.data.remote.HttpString
import io.github.illusion.mobileapp.data.remote.dto.LoginRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.LoginResponseDTO
import io.github.illusion.mobileapp.data.remote.dto.RegisterResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserApi(private val httpClient: HttpClient){
    suspend fun login(requestDTO: LoginRequestDTO): LoginResponseDTO {
        return httpClient.post("${HttpString.Url}/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDTO(requestDTO.email, requestDTO.password))
        }.body()
    }

    suspend fun register(requestDTO: LoginRequestDTO): RegisterResponseDTO {
        return httpClient.post("${HttpString.Url}/register") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDTO(requestDTO.email, requestDTO.password))
        }.body()
    }
}