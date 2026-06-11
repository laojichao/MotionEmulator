package com.zhufucdev.motion_emulator.ui.manager

import androidx.compose.runtime.Composable
import com.zhufucdev.me.stub.Data

abstract class EditableScreen(
    name: String,
    titleId: Int,
    iconId: Int
) : Screen(name, titleId, iconId) {
    @Composable
    abstract fun Editor(viewModel: EditorViewModel<*>, target: Data)
    @Composable
    abstract fun ListScreen(viewModel: EditorViewModel<*>)

    @Composable
    override fun List(viewModel: ManagerViewModel) {
        ListScreen(viewModel as EditorViewModel<*>)
    }

    object CellScreen : EditableScreen("cell", com.zhufucdev.motion_emulator.R.string.title_cells, com.zhufucdev.motion_emulator.R.drawable.ic_baseline_cell_tower_24) {
        @Composable
        override fun Editor(viewModel: EditorViewModel<*>, target: Data) {}
        @Composable
        override fun ListScreen(viewModel: EditorViewModel<*>) {}
    }

    object MotionScreen : EditableScreen("motion", com.zhufucdev.motion_emulator.R.string.title_motion, com.zhufucdev.motion_emulator.R.drawable.ic_baseline_smartphone_24) {
        @Composable
        override fun Editor(viewModel: EditorViewModel<*>, target: Data) {}
        @Composable
        override fun ListScreen(viewModel: EditorViewModel<*>) {}
    }

    object TraceScreen : EditableScreen("trace", com.zhufucdev.motion_emulator.R.string.title_trace, com.zhufucdev.motion_emulator.R.drawable.ic_baseline_map_24) {
        @Composable
        override fun Editor(viewModel: EditorViewModel<*>, target: Data) {}
        @Composable
        override fun ListScreen(viewModel: EditorViewModel<*>) {}
    }
}
