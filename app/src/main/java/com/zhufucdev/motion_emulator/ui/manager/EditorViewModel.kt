package com.zhufucdev.motion_emulator.ui.manager

import androidx.lifecycle.ViewModel

abstract class EditorViewModel : ManagerViewModel() {
    class MotionViewModel : EditorViewModel()
    class CellsViewModel : EditorViewModel()
    class TraceViewModel : EditorViewModel()
}
