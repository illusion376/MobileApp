package io.github.illusion.mobileapp.data.remote.api

import io.github.illusion.mobileapp.data.remote.HttpString
import io.github.illusion.mobileapp.data.remote.dto.PlayerCharacterDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PlayerApi(private val httpClient: HttpClient) {
    suspend fun getPlayer(userId : Int): PlayerCharacterDTO {
        return httpClient.get("${HttpString.URL}/character/$userId").body<PlayerCharacterDTO>()
    }
}