package io.github.illusion.mobileapp.domain.repository

import io.github.illusion.mobileapp.data.remote.dto.VerificationResponseDTO
import io.github.illusion.mobileapp.domain.model.RegisterStatus
import io.github.illusion.mobileapp.domain.model.User
import io.github.illusion.mobileapp.domain.model.VerificationStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, login: String, password: String): Result<RegisterStatus>
    fun getVerificationStatus(email: String): Flow<Result<VerificationResponseDTO>>
}