package com.zhufucdev.motion_emulator.ui.manager

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.navigation.NavGraphBuilder
import com.zhufucdev.motion_emulator.data.Cells
import com.zhufucdev.motion_emulator.data.DataStore
import com.zhufucdev.motion_emulator.data.Motions
import com.zhufucdev.motion_emulator.data.Traces
import com.zhufucdev.motion_emulator.extension.insert
import com.zhufucdev.me.stub.Data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class EditorViewModel<T : Data> : ManagerViewModel() {
    abstract val data: SnapshotStateList<T>
    abstract val editorScreen: EditableScreen

    override val screen: Screen get() = editorScreen

    abstract fun onClick(item: T)
    abstract fun onModify(item: T)
    abstract fun onRemove(item: T)

    open class DummyViewModel<T : Data>(
        override val editorScreen: EditableScreen,
        data: List<T>
    ) : EditorViewModel<T>() {
        private val coroutine by lazy { CoroutineScope(Dispatchers.Main) }
        override val data: SnapshotStateList<T> = data.toMutableStateList()

        override fun onCleared() {
            super.onCleared()
            coroutine.cancel("cleared")
        }

        override fun onClick(item: T) {
            runtime.navigationController.navigate("${screen.name}/${item.id}")
        }

        override fun onModify(item: T) {
            val index = data.indexOfFirst { it.id == item.id }
            data[index] = item
        }

        override fun onRemove(item: T) {
            val index = data.indexOf(item)
            data.remove(item)
            coroutine.launch {
                val result = runtime.snackbarHost.showSnackbar(
                    runtime.context.getString(com.zhufucdev.motion_emulator.R.string.text_deleted, item.id),
                    actionLabel = "Undo",
                    withDismissAction = true
                )
                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    undo(item, index)
                }
            }
        }

        open fun undo(item: T, index: Int) {
            data.insert(index, item)
        }
    }

    abstract class StandardViewModel<T : Data>(
        override val editorScreen: EditableScreen,
        private val store: DataStore<T>
    ) : DummyViewModel<T>(editorScreen, store.list()) {

        override fun onModify(item: T) {
            super.onModify(item)
            store.store(item, true)
        }

        override fun onRemove(item: T) {
            super.onRemove(item)
            store.delete(item, runtime.context)
        }

        override fun undo(item: T, index: Int) {
            super.undo(item, index)
            store.store(item, false)
        }
    }

    class CellsViewModel : StandardViewModel<com.zhufucdev.me.stub.CellTimeline>(
        EditableScreen.CellScreen, Cells
    )

    class MotionViewModel : StandardViewModel<com.zhufucdev.me.stub.Motion>(
        EditableScreen.MotionScreen, Motions
    )

    class TraceViewModel : StandardViewModel<com.zhufucdev.me.stub.Trace>(
        EditableScreen.TraceScreen, Traces
    )

    override fun compose(navGraphBuilder: NavGraphBuilder, runtimeArguments: RuntimeArguments) {
        // Compose navigation for editor - implementation restored from JEB
    }
}
