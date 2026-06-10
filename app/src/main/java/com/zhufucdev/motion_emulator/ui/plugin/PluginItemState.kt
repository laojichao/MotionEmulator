package com.zhufucdev.motion_emulator.ui.plugin

import com.zhufucdev.sdk.ProductQuery
import com.zhufucdev.sdk.ReleaseAsset

sealed class PluginItemState(val containsDownloadable: Boolean) {
    object None : PluginItemState(false)
    data class NotDownloaded(val query: ProductQuery) : PluginItemState(true)
    data class Update(val asset: ReleaseAsset) : PluginItemState(true)
}
