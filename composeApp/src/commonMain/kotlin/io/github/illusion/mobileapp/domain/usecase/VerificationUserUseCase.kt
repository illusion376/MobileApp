package io.github.illusion.mobileapp.domain.usecase

import io.github.illusion.mobileapp.data.remote.dto.VerificationResponseDTO
import io.github.illusion.mobileapp.domain.model.RegisterStatus
import io.github.illusion.mobileapp.domain.model.VerificationStatus
import io.github.illusion.mobileapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class VerificationUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): Flow<Result<VerificationResponseDTO>> {
        return repository.getVerificationStatus(email)
    }
}