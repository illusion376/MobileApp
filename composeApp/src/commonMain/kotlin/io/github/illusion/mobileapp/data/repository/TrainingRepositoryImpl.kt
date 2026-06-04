package io.github.illusion.mobileapp.data.repository

import io.github.illusion.mobileapp.data.mapper.toDomain
import io.github.illusion.mobileapp.data.remote.api.TrainingApi
import io.github.illusion.mobileapp.data.remote.dto.TrainingRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.TrainingResponseDTO
import io.github.illusion.mobileapp.domain.repository.KSafeRepository
import io.github.illusion.mobileapp.domain.repository.TrainingRepository

class TrainingRepositoryImpl(
    private val trainingApi: TrainingApi,
    private val kSafeRepository: KSafeRepository
) : TrainingRepository {

    override suspend fun finishWorkout(steps: Int, distanceKm: Double, durationMinutes: Int): Result<TrainingResponseDTO> {
        return runCatching {
            val userId = kSafeRepository.getDataOrNull("userId")
                ?: throw IllegalStateException("Пользователь не авторизован")

            trainingApi.finishWorkout(
                userId = userId,
                trainingRequestDTO = TrainingRequestDTO(
                    steps = steps,
                    distanceKm = distanceKm,
                    durationMinutes = durationMinutes
                )
            )
        }
    }
}