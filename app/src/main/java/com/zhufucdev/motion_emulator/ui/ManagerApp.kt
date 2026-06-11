package com.zhufucdev.motion_emulator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zhufucdev.me.stub.Data
import com.zhufucdev.motion_emulator.ui.model.ManagerViewModel

@Composable
fun ManagerApp(
    paddingValues: PaddingValues,
    viewModel: ManagerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
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
        
        LazyColumn {
            items(viewModel.data) { item ->
                ListItem(
                    headlineContent = { Text(item.id) },
                    supportingContent = { Text(item::class.simpleName ?: "Unknown") }
                )
            }
        }
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
