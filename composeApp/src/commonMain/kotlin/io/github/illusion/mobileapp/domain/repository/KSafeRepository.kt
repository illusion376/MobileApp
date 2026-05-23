package io.github.illusion.mobileapp.domain.repository

import io.github.illusion.mobileapp.resources.Res

interface KSafeRepository {
    fun saveData(key : String, value : String)

    fun getDataOrNull(key : String): String?
}