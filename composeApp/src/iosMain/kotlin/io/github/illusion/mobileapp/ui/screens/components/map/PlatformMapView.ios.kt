@file:OptIn(ExperimentalForeignApi::class)

package io.github.illusion.mobileapp.ui.screens.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKAnnotationProtocol
import platform.MapKit.MKAnnotationView
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKMarkerAnnotationView
import platform.MapKit.MKOverlayProtocol
import platform.MapKit.MKOverlayRenderer
import platform.MapKit.MKPointAnnotation
import platform.MapKit.MKPolyline
import platform.MapKit.MKPolylineRenderer
import platform.MapKit.addOverlay
import platform.MapKit.overlays
import platform.MapKit.removeOverlays
import platform.UIKit.UIColor
import platform.UIKit.UIUserInterfaceStyle
import platform.darwin.NSObject

private val AccentUIColor: UIColor
    get() = UIColor(red = 226.0 / 255.0, green = 213.0 / 255.0, blue = 102.0 / 255.0, alpha = 1.0)

private const val ROUTE_WIDTH = 5.0

private class MapDelegate : NSObject(), MKMapViewDelegateProtocol {

    override fun mapView(
        mapView: MKMapView,
        rendererForOverlay: MKOverlayProtocol,
    ): MKOverlayRenderer {
        val polyline = rendererForOverlay as? MKPolyline
        if (polyline != null) {
            val renderer = MKPolylineRenderer(polyline = polyline)
            renderer.strokeColor = AccentUIColor
            renderer.lineWidth = ROUTE_WIDTH
            return renderer
        }
        return MKOverlayRenderer(overlay = rendererForOverlay)
    }

    override fun mapView(
        mapView: MKMapView,
        viewForAnnotation: MKAnnotationProtocol,
    ): MKAnnotationView? {
        val marker = MKMarkerAnnotationView(
            annotation = viewForAnnotation,
            reuseIdentifier = "user_location",
        )
        marker.markerTintColor = AccentUIColor
        marker.glyphImage = null
        return marker
    }
}

@Composable
actual fun PlatformMapView(
    modifier: Modifier,
    userLatitude: Double,
    userLongitude: Double,
    routePoints: List<Pair<Double, Double>>,
    recenterTrigger: Int,
) {
    val delegate = remember { MapDelegate() }
    val userAnnotation = remember { MKPointAnnotation() }
    val mapView = remember {
        MKMapView().apply {
            setDelegate(delegate)
            showsUserLocation = false
            overrideUserInterfaceStyle = UIUserInterfaceStyle.UIUserInterfaceStyleDark
            setRegion(
                MKCoordinateRegionMakeWithDistance(
                    CLLocationCoordinate2DMake(userLatitude, userLongitude),
                    1000.0,
                    1000.0,
                ),
                animated = false,
            )
        }
    }

    UIKitView(
        factory = { mapView },
        modifier = modifier,
        update = { view ->
            view.removeAnnotation(userAnnotation)
            userAnnotation.setCoordinate(
                CLLocationCoordinate2DMake(userLatitude, userLongitude)
            )
            view.addAnnotation(userAnnotation)

            view.removeOverlays(view.overlays)
            if (routePoints.size >= 2) {
                memScoped {
                    val coords = allocArray<CLLocationCoordinate2D>(routePoints.size)
                    routePoints.forEachIndexed { index, point ->
                        coords[index].latitude = point.first
                        coords[index].longitude = point.second
                    }
                    val polyline = MKPolyline.polylineWithCoordinates(
                        coords = coords,
                        count = routePoints.size.toULong(),
                    )
                    view.addOverlay(polyline)
                }
            }
        },
    )

    LaunchedEffect(recenterTrigger) {
        if (recenterTrigger > 0 && (userLatitude != 0.0 || userLongitude != 0.0)) {
            mapView.setRegion(
                MKCoordinateRegionMakeWithDistance(
                    CLLocationCoordinate2DMake(userLatitude, userLongitude),
                    1000.0,
                    1000.0,
                ),
                animated = true,
            )
        }
    }
}