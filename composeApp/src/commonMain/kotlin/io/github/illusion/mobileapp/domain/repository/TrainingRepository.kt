package io.github.illusion.mobileapp.domain.repository

import io.github.illusion.mobileapp.data.remote.dto.TrainingResponceDTO

interface TrainingRepository {
    suspend fun finishWorkout(steps : Int, distanceKm : Double, durationMinutes : Int) : Result<TrainingResponceDTO>
}