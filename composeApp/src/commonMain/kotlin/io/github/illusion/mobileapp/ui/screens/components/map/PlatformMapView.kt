package io.github.illusion.mobileapp.ui.screens.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformMapView(
    modifier: Modifier,
    userLatitude: Double,
    userLongitude: Double,
    routePoints: List<Pair<Double, Double>>,
    recenterTrigger: Int,
)