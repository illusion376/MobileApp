package io.github.illusion.mobileapp.data.remote.api

import io.github.illusion.mobileapp.data.remote.HttpString
import io.github.illusion.mobileapp.data.remote.dto.PlayerCharacterDTO
import io.github.illusion.mobileapp.domain.repository.KSafeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class PlayerApi(private val httpClient: HttpClient, private val kSafeRepository: KSafeRepository) {
    suspend fun getPlayer(userId: String): PlayerCharacterDTO {
        val token = kSafeRepository.getDataOrNull("token")
            ?: throw IllegalStateException("Пользователь не авторизован")
        return httpClient.get("${HttpString.URL}/character/$userId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body<PlayerCharacterDTO>()
    }
}