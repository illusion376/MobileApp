package io.github.illusion.mobileapp.domain.repository

import io.github.illusion.mobileapp.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User
}