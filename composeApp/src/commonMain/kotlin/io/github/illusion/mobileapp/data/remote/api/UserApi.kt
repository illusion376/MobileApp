package io.github.illusion.mobileapp.data.remote.api

import io.github.illusion.mobileapp.data.remote.dto.LoginRequest
import io.github.illusion.mobileapp.data.remote.dto.LoginResponse
import io.github.illusion.mobileapp.domain.model.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class UserApi(private val httpClient: HttpClient){
    suspend fun login(email: String, password: String): User {
        return httpClient.post("/login") {
            setBody(LoginRequest(email, password))
        }.body()
    }
}