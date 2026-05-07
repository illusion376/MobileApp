package io.github.illusion.mobileapp.domain.usecase

import io.github.illusion.mobileapp.domain.model.RegisterStatus
import io.github.illusion.mobileapp.domain.model.VerificationStatus
import io.github.illusion.mobileapp.domain.repository.AuthRepository

class VerificationUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<VerificationStatus> {
        return repository.verification(email)
    }
}