package io.github.illusion.mobileapp.data.repository

import io.github.illusion.mobileapp.data.mapper.toDomain
import io.github.illusion.mobileapp.data.remote.api.UserApi
import io.github.illusion.mobileapp.data.remote.dto.LoginRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.LoginResponseDTO
import io.github.illusion.mobileapp.data.remote.dto.RegisterRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.RegisterResponseDTO
import io.github.illusion.mobileapp.data.remote.dto.VerificationRequestDTO
import io.github.illusion.mobileapp.data.remote.dto.VerificationResponseDTO
import io.github.illusion.mobileapp.domain.features.auth.TokenStorage
import io.github.illusion.mobileapp.domain.model.RegisterStatus
import io.github.illusion.mobileapp.domain.model.User
import io.github.illusion.mobileapp.domain.model.VerificationStatus
import io.github.illusion.mobileapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val userApi: UserApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<User> {
        return runCatching {
            val response = userApi.login(LoginRequestDTO(email, password))

            val token = response.token ?: throw Exception("Token is missing")

            val user = response.toDomain()

            tokenStorage.saveToken(token)
            user
        }
    }

    override suspend fun register(
        email: String,
        login: String,
        password: String
    ): Result<RegisterStatus> = runCatching {
        val response = userApi.register(RegisterRequestDTO(email, login, password))
        val status = response.status

        if(status == 201){
            RegisterStatus(status)
        }else{
            throw Exception("Register failed")
        }
    }

    override fun getVerificationStatus(email: String): Flow<Result<VerificationResponseDTO>> {
        return userApi.verification(VerificationRequestDTO(email.trim()))
    }

}