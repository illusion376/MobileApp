package io.github.illusion.mobileapp.domain.location

import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun startUpdates(): Flow<Pair<Double, Double>>
    fun stop()
}
