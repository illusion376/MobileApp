package io.github.illusion.mobileapp.data.repository

import eu.anifantakis.lib.ksafe.KSafe
import io.github.illusion.mobileapp.data.remote.api.UserApi
import io.github.illusion.mobileapp.domain.features.auth.TokenStorage
import io.github.illusion.mobileapp.domain.repository.KSafeRepository
import org.koin.compose.koinInject

class KSafeRepositoryImpl(
    private val kSafe: KSafe,
) : KSafeRepository {
    override fun saveData(key: String, value: String) {
        kSafe.putDirect(key, value)
    }

    override fun getDataOrNull(key: String): String? {
        return kSafe.getDirect(key, null)
    }
}