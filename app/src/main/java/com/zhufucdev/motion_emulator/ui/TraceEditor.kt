package com.zhufucdev.motion_emulator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zhufucdev.me.stub.Trace
import com.zhufucdev.motion_emulator.ui.model.ManagerViewModel

@Composable
fun TraceEditor(
    target: Trace,
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
        
        Text(
            text = "ID: ${target.id}",
            modifier = Modifier.padding(16.dp)
        )
        
        Text(
            text = "Points: ${target.points.size}",
            modifier = Modifier.padding(16.dp)
        )
    }
}
