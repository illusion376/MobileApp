package io.github.illusion.mobileapp.domain.model

data class Player(
    val userId: Int,
    val level: Int,
    val experience: Int,
    val strength: Int,
    val stamina: Int,
    val vitality: Int,
    val steps: Int,
    val streakDays: Int
)