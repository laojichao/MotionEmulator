package com.zhufucdev.motion_emulator.ui.map

import android.content.Context
import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.zhufucdev.motion_emulator.extension.ensureGoogleCoordinate
import com.zhufucdev.motion_emulator.extension.getAttrColor
import com.zhufucdev.motion_emulator.extension.isDarkModeEnabled
import com.zhufucdev.motion_emulator.extension.toGoogleLatLng
import com.zhufucdev.motion_emulator.extension.toPoint
import com.zhufucdev.me.stub.Point
import com.zhufucdev.me.stub.Trace

class GoogleMapsController(context: Context, private val gMap: GoogleMap) : MapController(context) {
    private var locationIndicator: ((Location) -> Unit)? = null

    override var displayStyle: MapStyle = MapStyle.NORMAL
        set(value) {
            field = value
            when (value) {
                MapStyle.NORMAL -> {
                    gMap.setMapStyle(null)
                    gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
                MapStyle.NIGHT -> {
                    val style = MapStyleOptions.loadRawResourceStyle(context, com.zhufucdev.motion_emulator.R.raw.mapstyle_night)
                    gMap.setMapStyle(style)
                    gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
                MapStyle.SATELLITE -> {
                    gMap.setMapStyle(null)
                    gMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
            }
        }

    override var displayType: MapDisplayType = MapDisplayType.INTERACTIVE
        set(value) {
            field = value
            val settings = gMap.uiSettings
            when (value) {
                MapDisplayType.STILL -> {
                    settings.isScrollGesturesEnabled = false
                    settings.isZoomGesturesEnabled = false
                    settings.isRotateGesturesEnabled = false
                }
                MapDisplayType.INTERACTIVE -> {
                    settings.isScrollGesturesEnabled = true
                    settings.isZoomGesturesEnabled = true
                    settings.isRotateGesturesEnabled = true
                }
            }
        }

    private val lineColor: Int get() = getAttrColor(com.google.android.material.R.attr.colorTertiary, context)

    init {
        displayStyle = if (isDarkModeEnabled(context.resources)) MapStyle.NIGHT else MapStyle.NORMAL
        gMap.isMyLocationEnabled = false
        gMap.uiSettings.isZoomControlsEnabled = false

        // Setup location indicator via LocationSource
        var listener: LocationSource.OnLocationChangedListener? = null
        gMap.setLocationSource(object : LocationSource {
            override fun activate(p0: LocationSource.OnLocationChangedListener) { listener = p0 }
            override fun deactivate() { listener = null }
        })
        gMap.isMyLocationEnabled = true
        gMap.uiSettings.isMyLocationButtonEnabled = false
        locationIndicator = { loc -> listener?.onLocationChanged(loc) }
        displayType = MapDisplayType.INTERACTIVE
    }

    override fun boundCamera(bounds: TraceBounds, animate: Boolean) {
        val update = CameraUpdateFactory.newLatLngBounds(bounds.google(), 40)
        if (animate) gMap.animateCamera(update) else gMap.moveCamera(update)
    }

    override fun cameraCenter(): Point {
        return gMap.cameraPosition.target.toPoint()
    }

    override fun drawTrace(trace: Trace): MapTraceCallback {
        val options = PolylineOptions().color(lineColor)
        val points = trace.points.map { it.ensureGoogleCoordinate().toGoogleLatLng() }
        options.addAll(points + trace.points[0].ensureGoogleCoordinate().toGoogleLatLng())
        val line = gMap.addPolyline(options)
        return MapTraceCallback { line.remove() }
    }

    override suspend fun getAddress(point: Point): String? {
        return com.zhufucdev.motion_emulator.extension.getAddressWithGoogle(
            point.ensureGoogleCoordinate().toGoogleLatLng(), context
        )
    }

    override fun moveCamera(location: Point, animate: Boolean, focus: Boolean) {
        val latLng = location.ensureGoogleCoordinate().toGoogleLatLng()
        val zoom = if (focus) 40f else 10f
        val update = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        if (animate) gMap.animateCamera(update) else gMap.moveCamera(update)
    }

    override fun project(x: Int, y: Int): Point {
        return gMap.projection.fromScreenLocation(android.graphics.Point(x, y)).toPoint()
    }

    override fun updateLocationIndicator(location: Location) {
        locationIndicator?.invoke(location)
    }

    override fun usePen(): MapScrawl {
        val polyline = PolylineOptions().color(lineColor)
        var lastPolyline: Polyline? = null
        var lastPos = LatLng(0.0, 0.0)
        val backStack = ArrayList<ArrayList<LatLng>>()

        return object : MapScrawl {
            override var element: Boolean = false

            override fun markBegin() { backStack.add(ArrayList()) }

            override fun addPoint(point: Point) {
                val latLng = point.ensureGoogleCoordinate().toGoogleLatLng()
                if (SphericalUtil.computeDistanceBetween(lastPos, latLng) >= 0.5) {
                    polyline.add(latLng)
                    backStack.lastOrNull()?.add(latLng)
                    lastPolyline?.remove()
                    lastPolyline = gMap.addPolyline(polyline)
                }
                lastPos = latLng
            }

            override fun getPoints(): List<Point> = polyline.points.map { it.toPoint() }

            override fun undo() {
                val last = backStack.removeLastOrNull() ?: return
                for (latLng in last) {
                    if (polyline.points.contains(latLng)) polyline.points.remove(latLng)
                    else polyline.add(latLng)
                }
                lastPolyline?.remove()
                lastPolyline = gMap.addPolyline(polyline)
                lastPos = polyline.points.lastOrNull() ?: LatLng(0.0, 0.0)
            }

            override fun clear() {
                val all = ArrayList<LatLng>()
                backStack.forEach { all.addAll(it) }
                backStack.add(all)
                lastPolyline?.remove()
                lastPolyline = null
                polyline.points.clear()
                lastPos = LatLng(0.0, 0.0)
            }
        }
    }
}
