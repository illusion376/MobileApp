package io.github.illusion.mobileapp.data.mapper

import io.github.illusion.mobileapp.data.remote.dto.LoginResponseDTO
import io.github.illusion.mobileapp.data.remote.dto.PlayerCharacterDTO
import io.github.illusion.mobileapp.data.remote.dto.RegisterResponseDTO
import io.github.illusion.mobileapp.domain.model.Player
import io.github.illusion.mobileapp.domain.model.User

fun LoginResponseDTO.toDomain(): User {
    return User(
        token = token ?: error("Token is missing")
    )
}

fun RegisterResponseDTO.toDomain(): User {
    return User(
        token = token ?: error("Token is missing")
    )
}

fun PlayerCharacterDTO.toDomain(): Player {
    return Player(
        userId = userId,
        level = level,
        experience = experience,
        strength = strength,
        stamina = stamina,
        vitality = vitality,
        steps = steps,
        streakDays = streakDays
    )
}