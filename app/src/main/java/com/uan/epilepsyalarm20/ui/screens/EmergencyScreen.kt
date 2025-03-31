package com.uan.epilepsyalarm20.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.domain.models.EmergencyViewModel
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes

@SuppressLint("MissingPermission")
@Composable
fun EmergencyScreen(
    viewModel: EmergencyViewModel,
    go: (Any) -> Unit,
) {
    val context = LocalContext.current
    val countdown by viewModel.countdown.collectAsState()
    val isEmergencyActive by viewModel.isEmergencyActive.collectAsState()
    val userInstructions = viewModel.getUserInstructions()

    val hasStartedCountdown by remember { derivedStateOf { isEmergencyActive || countdown < 5 } }

    LaunchedEffect(Unit)  {
        if (!hasStartedCountdown) {
            viewModel.startEmergencyCountdown()
        }
    }

    BackHandler {
        viewModel.cancelEmergency()
        val activity = context as? Activity
        activity?.moveTaskToBack(true)
        go(Routes.MenuPrincipal)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isEmergencyActive) {
            // UI cuando la emergencia está activa
            Text(
                text = "🚨 EMERGENCIA 🚨",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(60.dp))
            if(userInstructions != null) {
                Text(
                    text = "Instrucciones o datos adicionales del usuario:\n$userInstructions",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(60.dp))
            }
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
                val activity = context as? Activity
                activity?.moveTaskToBack(true)
                go(Routes.MenuPrincipal)
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
