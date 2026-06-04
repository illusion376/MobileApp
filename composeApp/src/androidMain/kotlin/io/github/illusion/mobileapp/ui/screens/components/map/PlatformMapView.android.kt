package io.github.illusion.mobileapp.ui.screens.components.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

private val ROUTE_COLOR = 0xFFE2D566.toInt()
private const val ROUTE_WIDTH = 5f
private const val USER_DOT_SIZE = 36
private val USER_DOT_COLOR = 0xFFE2D566.toInt()
private val USER_DOT_BORDER_COLOR = 0xFF1B1B15.toInt()

private fun createUserDotBitmap(): Bitmap {
    val bmp = Bitmap.createBitmap(USER_DOT_SIZE, USER_DOT_SIZE, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    val center = USER_DOT_SIZE / 2f
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    // Outer dark ring
    paint.color = USER_DOT_BORDER_COLOR
    paint.style = Paint.Style.FILL
    canvas.drawCircle(center, center, center, paint)
    // Inner yellow dot
    paint.color = USER_DOT_COLOR
    canvas.drawCircle(center, center, center - 4f, paint)
    return bmp
}

@Composable
actual fun PlatformMapView(
    modifier: Modifier,
    userLatitude: Double,
    userLongitude: Double,
    routePoints: List<Pair<Double, Double>>,
    recenterTrigger: Int,
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            onStart()
            mapWindow.map.isNightModeEnabled = true
            mapWindow.map.move(
                CameraPosition(Point(userLatitude, userLongitude), 16.0f, 0.0f, 0.0f)
            )
        }
    }

    val userDotImage = remember {
        ImageProvider.fromBitmap(createUserDotBitmap())
    }

    DisposableEffect(Unit) {
        onDispose {
            mapView.onStop()
        }
    }

    LaunchedEffect(recenterTrigger) {
        if (recenterTrigger > 0 && (userLatitude != 0.0 || userLongitude != 0.0)) {
            mapView.mapWindow.map.move(
                CameraPosition(Point(userLatitude, userLongitude), 16.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0.5f),
                null,
            )
        }
    }

    AndroidView(
        factory = { mapView },
        update = { view ->
            val map = view.mapWindow.map
            val target = Point(userLatitude, userLongitude)

            map.mapObjects.clear()

            if (routePoints.size >= 2) {
                val points = routePoints.map { Point(it.first, it.second) }
                val polyline = map.mapObjects.addPolyline(Polyline(points))
                polyline.setStrokeColor(ROUTE_COLOR)
                polyline.setStrokeWidth(ROUTE_WIDTH)
            }

            map.mapObjects.addPlacemark(target, userDotImage)
        },
        modifier = modifier,
    )
}