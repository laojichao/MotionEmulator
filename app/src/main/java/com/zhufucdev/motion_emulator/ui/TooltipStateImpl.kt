package com.zhufucdev.motion_emulator.ui

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class TooltipStateImpl : TooltipState {
    private var isVisibleState by mutableStateOf(false)
    private var position by mutableStateOf(Offset.Zero)
    private var opacity by mutableFloatStateOf(0f)
    private var content: (@Composable () -> Unit)? = null
    private var job: Job? = null

    override val isVisible: Boolean get() = isVisibleState

    override fun setVisible(value: Boolean) {
        isVisibleState = value
    }

    override fun dismiss() {
        setVisible(false)
        opacity = 0f
    }

    override suspend fun show(position: Offset, content: @Composable () -> Unit) {
        coroutineScope {
            this@TooltipStateImpl.position = position
            this@TooltipStateImpl.content = content
            job?.cancel()
            job = launch {
                setVisible(true)
                animate(
                    initialValue = opacity,
                    targetValue = 1f,
                    animationSpec = spring()
                ) { value, _ ->
                    opacity = value
                }
            }
            launch {
                job?.join()
                delay(1500)
                job = launch {
                    animate(
                        initialValue = opacity,
                        targetValue = 0f,
                        animationSpec = spring(stiffness = 2f)
                    ) { value, _ ->
                        opacity = value
                    }
                    setVisible(false)
                }
            }
        }
    }
}
