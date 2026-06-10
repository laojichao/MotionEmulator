package com.zhufucdev.motion_emulator.ui.map

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.SupportMapFragment
import com.zhufucdev.motion_emulator.R
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.LinkedHashSet

/**
 * 统一地图 Fragment
 *
 * 封装 AMap 和 Google Maps 两种地图实现，提供统一的接口。
 * 通过 [Provider] 枚举选择使用哪种地图引擎。
 *
 * ## 使用方式
 * 在 XML 布局中使用：
 * ```xml
 * <com.zhufucdev.motion_emulator.ui.map.UnifiedMapFragment
 *     android:id="@+id/map"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *     app:provider="gcp_maps" />
 * ```
 *
 * @param context Android Context
 * @param attrs XML 属性
 * @param defStyleAttr 默认样式属性
 */
class UnifiedMapFragment @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * 地图提供者枚举
     */
    enum class Provider {
        /** 高德地图 */
        AMAP,
        /** Google 地图 */
        GCP_MAPS
    }

    private val container = FrameLayout(context)

    /**
     * 获取当前地图控制器
     *
     * 在地图初始化完成后可用
     */
    var controller: MapController? = null
        private set

    /**
     * 获取/设置地图提供者
     *
     * 设置后会自动重新初始化地图
     */
    var provider: Provider = Provider.AMAP
        set(value) {
            if (field == value && controller != null) return
            removeAllViews()
            initializeAs(value)
            field = value
        }

    private val onReady = LinkedHashSet<(MapController) -> Unit>()

    init {
        addView(container)
        attrs?.let {
            val ta = context.theme.obtainStyledAttributes(it, R.styleable.UnifiedMapFragment, 0, 0)
            provider = Provider.values()[ta.getInteger(0, 0)]
            ta.recycle()
        }
    }

    private fun initializeAs(provider: Provider) {
        val fragment: Fragment = when (provider) {
            Provider.AMAP -> {
                val amapFragment = AMapFragment()
                amapFragment.getMapAsync { aMap ->
                    controller = AMapController(aMap, amapFragment.requireContext())
                    notifyReady(controller!!)
                }
                amapFragment
            }
            Provider.GCP_MAPS -> {
                val gMapFragment = SupportMapFragment.newInstance()
                gMapFragment.getMapAsync { googleMap ->
                    controller = GoogleMapsController(gMapFragment.requireContext(), googleMap)
                    notifyReady(controller!!)
                }
                gMapFragment
            }
        }
        val activity = context as? FragmentActivity
            ?: throw IllegalStateException("Can't initialize unless in a Fragment Activity context.")
        activity.supportFragmentManager.beginTransaction()
            .replace(this.id, fragment)
            .setReorderingAllowed(false)
            .commit()
    }

    private fun notifyReady(controller: MapController) {
        onReady.forEach { it(controller) }
        onReady.clear()
    }

    /**
     * 挂起函数，等待地图控制器就绪
     *
     * 如果控制器已经初始化，立即返回；否则等待初始化完成
     *
     * @return [MapController] 实例
     */
    suspend fun requireController(): MapController {
        controller?.let { return it }
        return suspendCancellableCoroutine { cont ->
            onReady.add { controller ->
                cont.resumeWith(Result.success(controller))
            }
        }
    }
}
