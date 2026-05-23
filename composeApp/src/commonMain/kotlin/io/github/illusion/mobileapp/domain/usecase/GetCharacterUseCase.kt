package io.github.illusion.mobileapp.domain.usecase

import io.github.illusion.mobileapp.domain.model.Player
import io.github.illusion.mobileapp.domain.model.User
import io.github.illusion.mobileapp.domain.repository.AuthRepository
import io.github.illusion.mobileapp.domain.repository.PlayerRepository

class GetCharacterUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(userId : Int): Result<Player> {
        return repository.getPlayer(userId)
    }
}