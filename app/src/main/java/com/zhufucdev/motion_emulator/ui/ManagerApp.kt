package com.zhufucdev.motion_emulator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zhufucdev.motion_emulator.ui.manager.LocalScreenProviders
import com.zhufucdev.motion_emulator.ui.manager.ManagerViewModel

@Composable
fun ManagerApp(
    paddingValues: PaddingValues,
    viewModel: ManagerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val screenProviders = LocalScreenProviders.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Text(
            text = "Data Manager",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun CellEditor(
    target: Any,
    paddingValues: PaddingValues,
    viewModel: ManagerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Text(
            text = "Cell Editor",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun MotionEditor(
    target: Any,
    paddingValues: PaddingValues,
    viewModel: ManagerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Text(
            text = "Motion Editor",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun TraceEditor(
    target: Any,
    paddingValues: PaddingValues,
    viewModel: ManagerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Text(
            text = "Trace Editor",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
