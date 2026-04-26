package io.github.illusion.mobileapp.data.remote.api

import io.github.illusion.mobileapp.data.remote.HttpString
import io.github.illusion.mobileapp.data.remote.dto.LoginRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.LoginResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class UserApi(private val httpClient: HttpClient){
    suspend fun login(requestDTO: LoginRequestDTO): LoginResponseDTO {
        return httpClient.post("${HttpString.Url}/login") {
            setBody(LoginRequestDTO(requestDTO.email, requestDTO.password))
        }.body()
    }
}