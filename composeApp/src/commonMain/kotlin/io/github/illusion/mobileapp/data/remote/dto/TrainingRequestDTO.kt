package io.github.illusion.mobileapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrainingRequestDTO(
    val steps: Int,
    val distanceKm: Double,
    val durationMinutes: Int
)