package io.github.illusion.mobileapp.data.repository

import io.github.illusion.mobileapp.data.remote.api.UserApi
import io.github.illusion.mobileapp.domain.model.User
import io.github.illusion.mobileapp.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val userApi: UserApi
) : AuthRepository {
    override suspend fun login(email: String, password: String): User {
        val response = userApi.login(email, password)
        return User(
            status = response.status
        )
    }
}