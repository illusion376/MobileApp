package io.github.illusion.mobileapp.domain.location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class IosLocationProvider : LocationProvider {
    override fun startUpdates(): Flow<Pair<Double, Double>> {
        // TODO: integrate CLLocationManager
        return flowOf(55.7558 to 37.6173)
    }

    override fun stop() = Unit
}
