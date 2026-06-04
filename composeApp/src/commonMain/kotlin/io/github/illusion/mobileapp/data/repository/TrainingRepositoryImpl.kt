package io.github.illusion.mobileapp.data.repository

import io.github.illusion.mobileapp.data.mapper.toDomain
import io.github.illusion.mobileapp.data.remote.api.TrainingApi
import io.github.illusion.mobileapp.data.remote.dto.TrainingRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.TrainingResponceDTO
import io.github.illusion.mobileapp.domain.repository.KSafeRepository
import io.github.illusion.mobileapp.domain.repository.TrainingRepository

class TrainingRepositoryImpl(private val trainingApi: TrainingApi, private val kSafeRepository: KSafeRepository) : TrainingRepository {
    override suspend fun finishWorkout(steps: Int, distanceKm: Double, durationMinutes: Int) : Result<TrainingResponceDTO> {
        return runCatching {

            val userId = kSafeRepository.getDataOrNull("userId")?: throw IllegalStateException("Пользователь не авторизован")

            val response = trainingApi.finishWorkout(userId, TrainingRequestDTO(steps, distanceKm, durationMinutes))

            response
        }
    }
}