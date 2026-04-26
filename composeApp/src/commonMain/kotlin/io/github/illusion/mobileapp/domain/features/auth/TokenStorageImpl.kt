package io.github.illusion.mobileapp.domain.features.auth

import com.russhwolf.settings.Settings

class TokenStorageImpl(
    private val settings: Settings
) : TokenStorage {

    override fun saveToken(token: String) {
        settings.putString("jwt_token", token)
    }

    override fun getToken(): String? {
        return settings.getStringOrNull("jwt_token")
    }

    override fun clearToken() {
        settings.remove("jwt_token")
    }
}