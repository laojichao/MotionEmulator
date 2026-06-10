package com.zhufucdev.motion_emulator.ui.map

/**
 * 地图交互模式枚举
 *
 * 控制地图的用户交互能力
 */
enum class MapDisplayType {
    /** 静态模式 - 禁用所有手势操作，用于预览显示 */
    STILL,
    /** 交互模式 - 启用缩放、平移、旋转等手势操作 */
    INTERACTIVE
}
