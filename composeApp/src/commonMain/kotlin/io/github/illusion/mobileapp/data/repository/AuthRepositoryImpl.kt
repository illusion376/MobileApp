package io.github.illusion.mobileapp.data.repository

import io.github.illusion.mobileapp.data.mapper.toDomain
import io.github.illusion.mobileapp.data.remote.api.UserApi
import io.github.illusion.mobileapp.data.remote.dto.LoginRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.LoginResponseDTO
import io.github.illusion.mobileapp.domain.features.auth.TokenStorage
import io.github.illusion.mobileapp.domain.model.User
import io.github.illusion.mobileapp.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val userApi: UserApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = userApi.login(
                LoginRequestDTO(email, password)
            )

            val token = response.token
                ?: return Result.failure(Exception("Token is missing"))

            tokenStorage.saveToken(token)

            Result.success(User(token))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}