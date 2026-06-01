package io.github.illusion.mobileapp.data.remote.api

import io.github.illusion.mobileapp.data.remote.HttpString
import io.github.illusion.mobileapp.data.remote.dto.QuestDTO
import io.github.illusion.mobileapp.domain.repository.KSafeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
private data class ActivateRequest(val questId: String)

class QuestApi(
    private val httpClient: HttpClient,
    private val kSafeRepository: KSafeRepository,
) {
    suspend fun getQuests(userId: String): List<QuestDTO> {
        val token = kSafeRepository.getDataOrNull("token")
            ?: throw IllegalStateException("Пользователь не авторизован")
        return httpClient.get("${HttpString.URL}/quests/$userId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    suspend fun activateQuest(userId: String, questId: String) {
        val token = kSafeRepository.getDataOrNull("token")
            ?: throw IllegalStateException("Пользователь не авторизован")
        httpClient.post("${HttpString.URL}/quests/$userId/activate") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(ActivateRequest(questId))
        }
    }
}
