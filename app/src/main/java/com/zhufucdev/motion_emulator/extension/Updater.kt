package com.zhufucdev.motion_emulator.extension

import android.content.Context
import com.zhufucdev.motion_emulator.BuildConfig

fun Updater(product: String, context: Context) = com.zhufucdev.update.AppUpdater(
    BuildConfig.server_uri,
    product,
    context,
)

fun Updater(context: Context) = Updater("me", context)
