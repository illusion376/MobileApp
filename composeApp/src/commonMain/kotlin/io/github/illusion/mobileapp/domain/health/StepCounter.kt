package io.github.illusion.mobileapp.domain.health

import kotlinx.coroutines.flow.Flow

interface StepCounter {
    fun start(): Flow<Int>
    fun stop()
}