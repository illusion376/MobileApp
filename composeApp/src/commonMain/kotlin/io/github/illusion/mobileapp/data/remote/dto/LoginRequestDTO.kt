package io.github.illusion.mobileapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
    val email: String,
    val password: String
)