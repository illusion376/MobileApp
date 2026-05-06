package io.github.illusion.mobileapp.domain.usecase

import io.github.illusion.mobileapp.data.remote.dto.RegisterResponseDTO
import io.github.illusion.mobileapp.domain.model.User
import io.github.illusion.mobileapp.domain.repository.AuthRepository

class RegisterUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, login: String, password: String): Result<RegisterResponseDTO> {
        return repository.register(email, login, password)
    }
}