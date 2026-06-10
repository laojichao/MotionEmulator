package com.zhufucdev.motion_emulator.ui.map

import android.content.Context
import android.graphics.Color
import android.location.Location
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.zhufucdev.motion_emulator.extension.ensureAmapCoordinate
import com.zhufucdev.motion_emulator.extension.getAttrColor
import com.zhufucdev.motion_emulator.extension.isDarkModeEnabled
import com.zhufucdev.motion_emulator.extension.toAmapLatLng
import com.zhufucdev.motion_emulator.extension.toPoint
import com.zhufucdev.me.stub.Point
import com.zhufucdev.me.stub.Trace
import kotlin.collections.ArrayList

class AMapController(private val aMap: AMap, context: Context) : MapController(context) {
    private var locationIndicator: Circle? = null
    private var accuracyIndicator: Circle? = null

    override var displayStyle: MapStyle = MapStyle.NORMAL
        set(value) {
            field = value
            aMap.mapType = when (value) {
                MapStyle.NORMAL, MapStyle.NIGHT -> 1
                MapStyle.SATELLITE -> 2
            }
        }

    override var displayType: MapDisplayType = MapDisplayType.INTERACTIVE
        set(value) {
            field = value
            val settings = aMap.uiSettings
            when (value) {
                MapDisplayType.STILL -> {
                    settings.isZoomGesturesEnabled = false
                    settings.isScrollGesturesEnabled = false
                    settings.isRotateGesturesEnabled = false
                }
                MapDisplayType.INTERACTIVE -> {
                    settings.isZoomGesturesEnabled = true
                    settings.isScrollGesturesEnabled = true
                    settings.isRotateGesturesEnabled = true
                }
            }
        }

    init {
        aMap.isMyLocationEnabled = false
        aMap.uiSettings.isZoomControlsEnabled = false
        val resources = context.resources
        if (isDarkModeEnabled(resources)) {
            aMap.mapType = 3
            displayStyle = MapStyle.NIGHT
        } else {
            displayStyle = MapStyle.NORMAL
        }
        displayType = MapDisplayType.INTERACTIVE
    }

    private val indicatorColor: Int get() = getAttrColor(com.google.android.material.R.attr.colorPrimary, context)
    private val indicatorStroke: Int
        get() = if (aMap.mapType == 1) Color.rgb(55, 71, 79) else Color.rgb(250, 250, 250)
    private val lineColor: Int get() = getAttrColor(com.google.android.material.R.attr.colorTertiary, context)

    override fun boundCamera(bounds: TraceBounds, animate: Boolean) {
        val update = CameraUpdateFactory.newLatLngBounds(bounds.amap(context), 400)
        if (animate) aMap.animateCamera(update) else aMap.moveCamera(update)
    }

    override fun cameraCenter(): Point {
        val target = aMap.cameraPosition.target
        return target.toPoint()
    }

    override fun drawTrace(trace: Trace): MapTraceCallback {
        val options = PolylineOptions()
        val points = trace.points.map { it.ensureAmapCoordinate(context).toAmapLatLng() }
        options.addAll(points + trace.points[0].ensureAmapCoordinate(context).toAmapLatLng())
        options.color(lineColor)
        val line = aMap.addPolyline(options)
        return MapTraceCallback { line.remove() }
    }

    override suspend fun getAddress(point: Point): String? {
        return com.zhufucdev.motion_emulator.extension.getAddressWithAmap(
            point.ensureAmapCoordinate(context).toAmapLatLng()
        )
    }

    override fun moveCamera(location: Point, animate: Boolean, focus: Boolean) {
        val latLng = location.ensureAmapCoordinate(context).toAmapLatLng()
        val zoom = if (focus) 40f else 10f
        val update = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        if (animate) aMap.animateCamera(update) else aMap.moveCamera(update)
    }

    override fun project(x: Int, y: Int): Point {
        return aMap.projection.fromScreenLocation(android.graphics.Point(x, y)).toPoint()
    }

    override fun updateLocationIndicator(location: Location) {
        val latLng = location.toPoint().ensureAmapCoordinate(context).toAmapLatLng()
        accuracyIndicator?.remove()
        accuracyIndicator = aMap.addCircle(
            CircleOptions().center(latLng)
                .strokeColor(0)
                .fillColor(Color.argb(100, 30, 136, 229))
                .radius(location.accuracy.toDouble())
        )
        redrawLocationIndicator(latLng)
    }

    private fun redrawLocationIndicator(latLng: LatLng) {
        locationIndicator?.remove()
        locationIndicator = aMap.addCircle(
            CircleOptions().center(latLng)
                .fillColor(indicatorColor)
                .strokeColor(indicatorStroke)
                .strokeWidth(5f)
                .radius(1048576.0 / Math.pow(2.0, aMap.cameraPosition.zoom.toDouble()))
                .zIndex(10f)
        )
    }

    override fun usePen(): MapScrawl {
        val polyline = PolylineOptions().color(lineColor)
        var lastPolyline: Polyline? = null
        var lastPos = LatLng(0.0, 0.0)
        val backStack = ArrayList<ArrayList<LatLng>>()

        return object : MapScrawl {
            override var element: Boolean = false

            override fun markBegin() {
                backStack.add(ArrayList())
            }

            override fun addPoint(point: Point) {
                val latLng = point.ensureAmapCoordinate(context).toAmapLatLng()
                if (AMapUtils.calculateLineDistance(lastPos, latLng) >= 0.5f) {
                    polyline.add(latLng)
                    backStack.lastOrNull()?.add(latLng)
                    lastPolyline?.remove()
                    lastPolyline = aMap.addPolyline(polyline)
                }
                lastPos = latLng
            }

            override fun getPoints(): List<Point> {
                return polyline.points.map { it.toPoint() }
            }

            override fun undo() {
                val last = backStack.removeLastOrNull() ?: return
                for (latLng in last) {
                    if (polyline.points.contains(latLng)) polyline.points.remove(latLng)
                    else polyline.add(latLng)
                }
                lastPolyline?.remove()
                lastPolyline = aMap.addPolyline(polyline)
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
