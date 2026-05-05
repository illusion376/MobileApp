package io.github.illusion.mobileapp.data.mapper

import io.github.illusion.mobileapp.data.remote.dto.LoginResponseDTO
import io.github.illusion.mobileapp.domain.model.User

fun LoginResponseDTO.toDomain(): User {
    return User(
        token = token ?: error("Token is missing")
    )
}