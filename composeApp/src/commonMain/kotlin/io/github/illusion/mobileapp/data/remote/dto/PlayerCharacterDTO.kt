package io.github.illusion.mobileapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlayerCharacterDTO(
    val userId: Int,
    val level: Int,
    val experience: Int,
    val strength: Int,
    val stamina: Int,
    val vitality: Int,
    val steps: Int,
    val streakDays: Int
)