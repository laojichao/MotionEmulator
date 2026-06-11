package com.zhufucdev.motion_emulator.extension

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.AppBarLayout
import com.zhufucdev.motion_emulator.provider.EmulationRef
import com.zhufucdev.me.stub.BlockBox
import com.zhufucdev.me.stub.Box
import com.zhufucdev.me.stub.CoordinateSystem
import com.zhufucdev.me.stub.Data
import com.zhufucdev.me.stub.EmptyBox
import com.zhufucdev.me.stub.Emulation
import com.zhufucdev.me.stub.Motion
import com.zhufucdev.me.stub.MotionMoment
import com.zhufucdev.me.stub.Point
import java.math.RoundingMode
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * 检查应用是否为系统应用
 *
 * 通过 ApplicationInfo.flags 判断应用是否具有 FLAG_SYSTEM 标志
 */
val ApplicationInfo.isSystemApp: Boolean
    get() = flags and ApplicationInfo.FLAG_SYSTEM != 0

/**
 * 安全地在指定位置插入元素到列表
 *
 * 如果 [index] 超出列表大小，则追加到末尾，避免 [IndexOutOfBoundsException]
 *
 * @param T 列表元素类型
 * @param index 插入位置
 * @param element 要插入的元素
 */
fun <T> MutableList<T>.insert(index: Int, element: T) {
    if (index >= size) {
        add(element)
    } else {
        add(index, element)
    }
}

/**
 * 为刘海屏设备调整 Toolbar 的边距
 *
 * 在 Android P (API 28) 及以上版本中，检测 DisplayCutout 并设置
 * AppBarLayout 的顶部内边距以避开刘海区域
 *
 * @param activity 当前 Activity
 * @param appBarLayout 需要调整的 AppBarLayout
 */
fun adjustToolbarMarginForNotch(activity: Activity, appBarLayout: AppBarLayout) {
    if (Build.VERSION.SDK_INT >= 28) {
        val windowInsets = activity.window.decorView.rootWindowInsets
        if (windowInsets != null) {
            val displayCutout = windowInsets.displayCutout
            if (displayCutout != null) {
                appBarLayout.setPadding(0, displayCutout.safeInsetTop, 0, 0)
            }
        }
    }
}

/**
 * 将时间戳格式化为字符串
 *
 * @param dateFormat 日期格式化器
 * @param time 时间戳（毫秒），默认为当前时间
 * @return 格式化后的日期字符串
 */
fun dateString(dateFormat: DateFormat, time: Long = System.currentTimeMillis()): String {
    return dateFormat.format(Date(time))
}

/**
 * 从 Context 获取有效的时间格式
 *
 * 读取用户偏好设置中的时间格式配置，如果用户启用了自定义格式则使用之，
 * 否则使用系统默认的日期时间格式
 *
 * @param context Android Context
 * @return 配置好的 [DateFormat] 实例
 */
fun effectiveTimeFormat(context: Context): DateFormat {
    val preferences by lazySharedPreferences(context)
    return effectiveTimeFormat(preferences)
}

/**
 * 从 SharedPreferences 获取有效的时间格式
 *
 * @param preferences SharedPreferences 实例
 * @return 配置好的 [DateFormat] 实例
 */
fun effectiveTimeFormat(preferences: SharedPreferences): DateFormat {
    return if (preferences.getBoolean("customize_time_format", false)) {
        SimpleDateFormat(preferences.getString("time_format", "dd-MM-yyyy hh:mm:ss"), Locale.getDefault())
    } else {
        SimpleDateFormat.getDateTimeInstance()
    }
}

/**
 * 估算运动速度
 *
 * 基于步数传感器（TYPE_STEP_COUNTER 或 TYPE_STEP_DETECTOR）的数据计算平均速度
 * 使用 1.2 米作为平均步长
 *
 * @param motion 运动数据
 * @return 估算的速度（米/秒），如果无法估算则返回 null
 */
fun estimateSpeed(motion: Motion): Double? {
    val stepCounter = motion.sensorsInvolved.contains(19) // TYPE_STEP_COUNTER
    val stepDetector = motion.sensorsInvolved.contains(18) // TYPE_STEP_DETECTOR

    if (!stepCounter && !stepDetector) return null

    if (stepCounter) {
        var last: MotionMoment? = null
        var total = 0.0
        var count = 0
        for (moment in motion.moments) {
            if (!moment.data.containsKey(19)) continue
            if (last == null) {
                last = moment
            } else {
                val current = (moment.data[19] as? FloatArray)?.firstOrNull() ?: continue
                val previous = (last.data[19] as? FloatArray)?.firstOrNull() ?: continue
                total += (current - previous).toDouble() * 1.2 / (moment.elapsed - last.elapsed).toDouble()
                count++
                last = moment
            }
        }
        return if (total < 0 || total.isNaN() || count <= 0) null else total / count.toDouble()
    } else {
        var last: MotionMoment? = null
        var total = 0.0
        var count = 0
        for (moment in motion.moments) {
            if (!moment.data.containsKey(18)) continue
            if (last == null) {
                last = moment
            } else {
                total += 1.2 / (moment.elapsed - last.elapsed).toDouble()
                count++
                last = moment
            }
        }
        return if (total < 0 || total.isNaN() || count <= 0) null else total / count.toDouble()
    }
}

/**
 * 估算运动持续时间
 *
 * 计算第一个和最后一个 MotionMoment 之间的时间差
 *
 * @param motion 运动数据
 * @return 持续时间 [Duration]，如果数据不足则返回 0 秒
 */
fun estimateTimespan(motion: Motion): Duration {
    return if (motion.moments.size >= 2) {
        ((motion.moments.last().elapsed - motion.moments.first().elapsed) * 1.0).toDuration(DurationUnit.SECONDS)
    } else {
        0.toDuration(DurationUnit.SECONDS)
    }
}

/**
 * 初始化 Toolbar 并设置导航支持
 *
 * 将 Toolbar 设置为 Activity 的 ActionBar，并配置返回按钮的点击行为：
 * - 如果提供了 NavController，优先尝试导航返回
 * - 否则直接结束 Activity
 *
 * @param activity AppCompatActivity 实例
 * @param toolbar 要初始化的 Toolbar
 * @param navController 可选的 NavController，用于导航返回
 */
fun initializeToolbar(activity: AppCompatActivity, toolbar: Toolbar, navController: NavController? = null) {
    activity.setSupportActionBar(toolbar)
    toolbar.setNavigationOnClickListener {
        if (navController == null || !navController.navigateUp()) {
            activity.finish()
        }
    }
}

/**
 * 创建懒加载的 SharedPreferences 实例（基于 Context）
 *
 * @param context Android Context
 * @return [Lazy] 委托的 SharedPreferences
 */
fun lazySharedPreferences(context: Context): Lazy<SharedPreferences> {
    return lazy { sharedPreferences(context) }
}

/**
 * 创建懒加载的 SharedPreferences 实例（基于 Fragment）
 *
 * 会在 Fragment 附加到 Activity 后才获取 Context
 *
 * @param fragment Fragment 实例
 * @return [Lazy] 委托的 SharedPreferences
 */
fun lazySharedPreferences(fragment: Fragment): Lazy<SharedPreferences> {
    return lazy { sharedPreferences(fragment.requireContext()) }
}

/**
 * 将 Emulation 转换为 EmulationRef
 *
 * 提取 Emulation 中各组件的引用 ID，用于持久化存储默认配置
 *
 * @param emulation 模拟实例
 * @return 包含各组件引用 ID 的 [EmulationRef]
 */
fun ref(emulation: Emulation): EmulationRef {
    return EmulationRef(
        emulation.trace.id,
        ref(emulation.motion),
        ref(emulation.cells),
        emulation.velocity,
        emulation.repeat,
        emulation.satelliteCount
    )
}

/**
 * 将 Box 转换为引用字符串
 *
 * - [EmptyBox] → "none"
 * - [BlockBox] → "block"
 * - 其他 → 内部 Data 对象的 id
 *
 * @param box 数据容器
 * @return 引用字符串
 */
fun ref(box: Box<*>): String {
    return when (box) {
        is EmptyBox -> "none"
        is BlockBox -> "block"
        else -> (box.value as? Data)?.id ?: "null"
    }
}

/**
 * 设置状态栏为沉浸式模式
 *
 * 启用边缘到边缘显示，将状态栏和导航栏颜色设为透明
 *
 * @param activity 当前 Activity
 */
fun setUpStatusBar(activity: Activity) {
    WindowCompat.setDecorFitsSystemWindows(activity.window, false)
    activity.window.statusBarColor = 0
    activity.window.navigationBarColor = 0
}

/**
 * 获取默认的 SharedPreferences 实例
 *
 * @param context Android Context
 * @return 默认 SharedPreferences
 */
fun sharedPreferences(context: Context): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
}

/**
 * 将浮点数格式化为指定小数位数的字符串
 *
 * 使用 [DecimalFormat] 和 [RoundingMode.HALF_UP] 进行四舍五入
 *
 * @param value 要格式化的浮点数
 * @param n 保留的小数位数
 * @return 格式化后的字符串
 */
fun toFixed(value: Float, n: Int): String {
    val pattern = buildString {
        append("#.")
        repeat(n) { append("#") }
    }
    val format = DecimalFormat(pattern)
    format.roundingMode = RoundingMode.HALF_UP
    return format.format(value.toDouble())
}

/**
 * 将 Android Location 转换为 stub Point
 *
 * 使用 WGS84 坐标系统
 *
 * @param location Android 位置对象
 * @return 对应的 [Point] 对象
 */
fun toPoint(location: Location): Point {
    return Point(location.latitude, location.longitude, CoordinateSystem.WGS84)
}
