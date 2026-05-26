package io.github.illusion.mobileapp.ui.screens.components.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun PlatformMapView(
    modifier: Modifier,
    userLatitude: Double,
    userLongitude: Double,
    routePoints: List<Pair<Double, Double>>,
    onMyLocationClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF212121)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Map (iOS — MKMapView integration pending)",
            color = Color(0xFF757575),
        )
    }
}
