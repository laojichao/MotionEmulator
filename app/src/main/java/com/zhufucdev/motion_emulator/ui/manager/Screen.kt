package com.zhufucdev.motion_emulator.ui.manager

import androidx.compose.runtime.Composable

abstract class Screen(
    val name: String,
    val titleId: Int,
    val iconId: Int
) {
    @Composable
    abstract fun List(viewModel: ManagerViewModel)

    object OverviewScreen : Screen("overview", com.zhufucdev.motion_emulator.R.string.title_overview, com.zhufucdev.motion_emulator.R.drawable.ic_baseline_grid_view_24) {
        @Composable
        override fun List(viewModel: ManagerViewModel) {
            // Compose UI - OverviewScreen
        }
    }
}
