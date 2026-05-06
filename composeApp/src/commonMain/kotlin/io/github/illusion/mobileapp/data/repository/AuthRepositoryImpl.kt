package io.github.illusion.mobileapp.data.repository

import io.github.illusion.mobileapp.data.mapper.toDomain
import io.github.illusion.mobileapp.data.remote.api.UserApi
import io.github.illusion.mobileapp.data.remote.dto.LoginRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.LoginResponseDTO
import io.github.illusion.mobileapp.data.remote.dto.RegisterResponseDTO
import io.github.illusion.mobileapp.domain.features.auth.TokenStorage
import io.github.illusion.mobileapp.domain.model.User
import io.github.illusion.mobileapp.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val userApi: UserApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<User> {
        return try {

            val response = try {
                userApi.login(LoginRequestDTO(email, password))
            } catch (e: Exception) {
                println("API error: ${e.message}")
                throw e
            }

            val token = response.token
                ?: return Result.failure(Exception("Token is missing"))

            tokenStorage.saveToken(token)
            Result.success(User(token))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        email: String,
        login: String,
        password: String
    ): Result<RegisterResponseDTO> {
        return try {

            val response = try {
                userApi.register(LoginRequestDTO(email, password))
            } catch (e: Exception) {
                println("API error: ${e.message}")
                throw e
            }

            Result.success(RegisterResponseDTO(response.status, response.message, response.token))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}