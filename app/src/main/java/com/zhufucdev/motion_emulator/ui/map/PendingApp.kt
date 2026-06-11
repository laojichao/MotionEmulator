package com.zhufucdev.motion_emulator.ui.map

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhufucdev.motion_emulator.R
import com.zhufucdev.motion_emulator.ui.map.UnifiedMapFragment.Provider
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingApp(
    proceeding: Class<*>,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    var targetProvider by remember { mutableStateOf(Provider.GCP_MAPS) }
    val preference = remember { context.getSharedPreferences("settings", Context.MODE_PRIVATE) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_activity_map_pending)) },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Text(text = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = R.string.title_is_gcs_accessible),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(24.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = targetProvider == Provider.GCP_MAPS,
                    onClick = { targetProvider = Provider.GCP_MAPS }
                )
                Column {
                    Text(
                        text = "Google Maps",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = targetProvider == Provider.AMAP,
                    onClick = { targetProvider = Provider.AMAP }
                )
                Column {
                    Text(
                        text = "AMap",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Button(
                onClick = {
                    val editor = preference.edit()
                    val providerName = targetProvider.name.lowercase(Locale.ROOT)
                    editor.putString("map_provider", providerName)
                    editor.putString("poi_provider", providerName)
                    editor.apply()
                    val intent = Intent(context, proceeding)
                    context.startActivity(intent)
                    onFinish()
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 12.dp, bottom = 12.dp)
            ) {
                Text(text = "Continue")
            }
        }
    }
}
