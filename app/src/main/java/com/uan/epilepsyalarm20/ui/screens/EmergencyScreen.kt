package com.uan.epilepsyalarm20.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.domain.models.EmergencyViewModel
import kotlin.system.exitProcess

// Screen de ejemplo para obtener la ubicación
//@Preview(showBackground = true)
@Composable
fun EmergencyScreen(
    viewModel: EmergencyViewModel
) {
    val context = LocalContext.current
    val mapsLink by viewModel.mapsLink.collectAsState()
    val countdown by viewModel.countdown.collectAsState()
    val isEmergencyActive by viewModel.isEmergencyActive.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startEmergencyCountdown()
    }

    BackHandler { // Detecta el botón "Atrás" y cierra la app
        viewModel.cancelEmergency()
        (context as? Activity)?.finish()
        exitProcess(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isEmergencyActive) {
            // UI cuando la emergencia está activa
            Text(
                text = "🚨 EMERGENCIA ACTIVADA 🚨",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            // UI durante la cuenta regresiva
            Text(
                text = "Tiempo restante: $countdown",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            onClick = {
                viewModel.cancelEmergency()
                (context as? Activity)?.finish() // Cierra la app
                System.exit(0) // Asegura que se cierre completamente
            }
        ) {
            Text(
                "Detener Alarma",
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
