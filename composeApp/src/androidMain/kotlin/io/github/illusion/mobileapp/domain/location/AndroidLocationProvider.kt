package io.github.illusion.mobileapp.domain.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidLocationProvider(private val context: Context) : LocationProvider {

    @SuppressLint("MissingPermission")
    override fun startUpdates(): Flow<Pair<Double, Double>> = callbackFlow {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            val lastGps = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val lastNet = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val last = lastGps ?: lastNet
            if (last != null) {
                trySend(last.latitude to last.longitude)
            }
        } catch (_: SecurityException) { }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                trySend(location.latitude to location.longitude)
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) = Unit
            override fun onProviderEnabled(provider: String) = Unit
            override fun onProviderDisabled(provider: String) = Unit
        }

        try {
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000L,
                    2f,
                    listener,
                )
            }
            if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                lm.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    2000L,
                    2f,
                    listener,
                )
            }
        } catch (_: SecurityException) {
            close()
        }

        awaitClose {
            try {
                lm.removeUpdates(listener)
            } catch (_: Exception) { }
        }
    }

    override fun stop() = Unit
}
