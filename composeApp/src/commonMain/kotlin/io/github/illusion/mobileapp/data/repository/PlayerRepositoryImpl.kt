package io.github.illusion.mobileapp.data.repository

import io.github.illusion.mobileapp.data.mapper.toDomain
import io.github.illusion.mobileapp.data.remote.api.PlayerApi
import io.github.illusion.mobileapp.data.remote.dto.LoginRequestDTO
import io.github.illusion.mobileapp.domain.model.Player
import io.github.illusion.mobileapp.domain.repository.PlayerRepository

class PlayerRepositoryImpl(private val playerApi: PlayerApi) : PlayerRepository{
    override suspend fun getPlayer(userId: Int): Result<Player> {
        return runCatching {
            val response = playerApi.getPlayer(userId)

            val player = response.toDomain()

            player
        }
    }

}