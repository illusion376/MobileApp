package io.github.illusion.mobileapp.domain.features.auth

interface TokenStorage {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}