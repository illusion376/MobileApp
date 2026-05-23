package io.github.illusion.mobileapp.domain.usecase

import io.github.illusion.mobileapp.domain.model.User
import io.github.illusion.mobileapp.domain.repository.AuthRepository
import io.github.illusion.mobileapp.domain.repository.KSafeRepository

class LoginUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.login(email, password)
    }
}
