package io.github.illusion.mobileapp.domain.health

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreMotion.CMPedometer
import platform.CoreMotion.CMPedometerData
import platform.Foundation.NSDate
import platform.Foundation.NSError

class IosPedometerStepCounter : StepCounter {

    private val pedometer = CMPedometer()

    override fun start(): Flow<Int> = callbackFlow {
        if (!CMPedometer.isStepCountingAvailable()) {
            trySend(0)
            close()
            return@callbackFlow
        }

        pedometer.startPedometerUpdatesFromDate(NSDate()) { data: CMPedometerData?, _: NSError? ->
            val steps = data?.numberOfSteps?.intValue ?: 0
            trySend(steps)
        }

        awaitClose {
            pedometer.stopPedometerUpdates()
        }
    }

    override fun stop() {
        pedometer.stopPedometerUpdates()
    }
}