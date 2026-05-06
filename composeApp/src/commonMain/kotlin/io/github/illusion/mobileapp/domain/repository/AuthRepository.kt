package io.github.illusion.mobileapp.domain.repository

import io.github.illusion.mobileapp.data.remote.dto.LoginResponseDTO
import io.github.illusion.mobileapp.data.remote.dto.RegisterResponseDTO
import io.github.illusion.mobileapp.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, login: String, password: String): Result<RegisterResponseDTO>
}