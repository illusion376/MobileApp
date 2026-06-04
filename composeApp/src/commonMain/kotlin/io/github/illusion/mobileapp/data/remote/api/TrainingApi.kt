package io.github.illusion.mobileapp.data.remote.api

import io.github.illusion.mobileapp.data.remote.HttpString
import io.github.illusion.mobileapp.data.remote.dto.PlayerCharacterDTO
import io.github.illusion.mobileapp.data.remote.dto.TrainingRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.TrainingResponseDTO
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

class TrainingApi(private val httpClient: HttpClient, private val kSafeRepository: KSafeRepository) {
    suspend fun finishWorkout(userId: String, trainingRequestDTO: TrainingRequestDTO): TrainingResponseDTO {
        val token = kSafeRepository.getDataOrNull("token")
            ?: throw IllegalStateException("Пользователь не авторизован")

        return httpClient.post("${HttpString.URL}/training/finish/$userId") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(trainingRequestDTO)
        }.body<TrainingResponseDTO>()
    }

}