package io.github.illusion.mobileapp.domain.repository

import io.github.illusion.mobileapp.domain.model.RegisterStatus
import io.github.illusion.mobileapp.domain.model.User
import io.github.illusion.mobileapp.domain.model.VerificationStatus

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, login: String, password: String): Result<RegisterStatus>
    suspend fun verification(email: String): Result<VerificationStatus>

}