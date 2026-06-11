package com.zhufucdev.motion_emulator.ui.manager

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zhufucdev.motion_emulator.ui.component.BottomSheetModalState

abstract class ManagerViewModel : ViewModel() {
    lateinit var runtime: RuntimeArguments

    abstract fun compose(navGraphBuilder: NavGraphBuilder, runtimeArguments: RuntimeArguments)

    abstract val screen: Screen

    data class RuntimeArguments(
        val snackbarHost: SnackbarHostState,
        val navigationController: NavController,
        val context: Context,
        val bottomModalState: BottomSheetModalState
    )

    class OverviewViewModel : ManagerViewModel() {
        override val screen: Screen.OverviewScreen get() = Screen.OverviewScreen

        override fun compose(navGraphBuilder: NavGraphBuilder, runtimeArguments: RuntimeArguments) {
            // Compose navigation for overview - implementation restored from JEB
        }
    }
}
