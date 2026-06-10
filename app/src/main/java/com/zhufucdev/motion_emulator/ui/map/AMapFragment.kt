package com.zhufucdev.motion_emulator.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView

class AMapFragment : Fragment() {
    private var getter: ((AMap) -> Unit)? = null
    private lateinit var map: MapView

    fun getMapAsync(getter: (AMap) -> Unit) {
        this.getter = getter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        map = MapView(container?.context)
        map.onCreate(savedInstanceState)
        getter?.invoke(map.map)
        return map
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }
}
