package com.uan.epilepsyalarm20.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.domain.models.EmergencyViewModel

// Screen de ejemplo para obtener la ubicación
@Composable
fun EmergencyScreen(viewModel: EmergencyViewModel) {
    val mapsLink by viewModel.mapsLink.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        mapsLink?.let {
            Text("Ubicación: $it")
        }

        Button(onClick = { viewModel.emergencyButtonOnClick() }) {
            Text("Enviar mensaje")
        }
    }
}