package io.github.illusion.mobileapp.domain.repository

import io.github.illusion.mobileapp.domain.model.Player
import io.github.illusion.mobileapp.domain.model.User

interface PlayerRepository {
    suspend fun getPlayer(userId : Int): Result<Player>
}