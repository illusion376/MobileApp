package io.github.illusion.mobileapp.domain.repository

import io.github.illusion.mobileapp.data.remote.dto.TrainingResponseDTO

interface TrainingRepository {
    suspend fun finishWorkout(steps : Int, distanceKm : Double, durationMinutes : Int) : Result<TrainingResponseDTO>
}