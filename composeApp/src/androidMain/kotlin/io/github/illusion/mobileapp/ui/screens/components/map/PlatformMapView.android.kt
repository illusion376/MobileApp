package io.github.illusion.mobileapp.ui.screens.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

@Composable
actual fun PlatformMapView(
    modifier: Modifier,
    userLatitude: Double,
    userLongitude: Double,
    routePoints: List<Pair<Double, Double>>,
    onMyLocationClick: () -> Unit,
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            onStart()
            mapWindow.map.isNightModeEnabled = true
            mapWindow.map.move(
                CameraPosition(Point(userLatitude, userLongitude), 15.0f, 0.0f, 0.0f)
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mapView.onStop()
        }
    }

    AndroidView(
        factory = { mapView },
        update = { view ->
            val map = view.mapWindow.map
            val target = Point(userLatitude, userLongitude)

            map.move(
                CameraPosition(target, map.cameraPosition.zoom.coerceAtLeast(15.0f), 0.0f, 0.0f)
            )

            map.mapObjects.clear()

            if (routePoints.size >= 2) {
                val points = routePoints.map { Point(it.first, it.second) }
                val polyline = map.mapObjects.addPolyline(Polyline(points))
                polyline.setStrokeColor(0xFFE2D566.toInt())
                polyline.setStrokeWidth(4f)
            }

            map.mapObjects.addPlacemark(target)
        },
        modifier = modifier,
    )
}
