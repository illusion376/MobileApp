package io.github.illusion.mobileapp.domain.usecase

import io.github.illusion.mobileapp.domain.model.Player
import io.github.illusion.mobileapp.domain.model.User
import io.github.illusion.mobileapp.domain.repository.AuthRepository
import io.github.illusion.mobileapp.domain.repository.PlayerRepository
import io.github.illusion.mobileapp.resources.Res

class GetCharacterUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(userId : String): Result<Player> {
        return repository.getPlayer(userId)
    }
}