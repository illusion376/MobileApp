package io.github.illusion.mobileapp.domain.usecase

import io.github.illusion.mobileapp.data.remote.dto.TrainingResponseDTO
import io.github.illusion.mobileapp.domain.model.Player
import io.github.illusion.mobileapp.domain.repository.PlayerRepository
import io.github.illusion.mobileapp.domain.repository.TrainingRepository

class FinishTrainingUseCase(
    private val repository: TrainingRepository
) {
    suspend operator fun invoke(steps : Int, distanceKm : Double, durationMinutes : Int): Result<TrainingResponseDTO> {
        return repository.finishWorkout(steps, distanceKm, durationMinutes)
    }
}