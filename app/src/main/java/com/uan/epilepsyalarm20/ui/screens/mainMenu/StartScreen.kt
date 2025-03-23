package com.uan.epilepsyalarm20.ui.screens.mainMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.domain.models.StartViewModel
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.SuccessSnackbar
import com.uan.epilepsyalarm20.ui.theme.textFieldColors

@Composable
fun StartScreen(viewModel: StartViewModel) {

    var message by rememberSaveable { mutableStateOf("") }
    var instructions by rememberSaveable { mutableStateOf("") }

    var showSuccessMessage by remember { mutableStateOf(false) }

    SuccessSnackbar(
        showMessage = showSuccessMessage,
        onDismiss = { showSuccessMessage = false }
    )

    LaunchedEffect(Unit) {
        message = viewModel.getEmergencyMessage() ?: ""
        instructions = viewModel.getEmergencyInstructions() ?: ""
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Mensaje de Alerta") },
            placeholder = { Text(text = "Escribe el mensaje que aparecerá en pantalla cuando se active la alerta.") },
            colors = textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        CustomButton(text = "Guardar") {
            if(message.isNotEmpty()){
                viewModel.updateEmergencyMessage(message)
                showSuccessMessage = true
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = instructions,
            onValueChange = { instructions = it },
            label = { Text("Instrucciones o Datos Adicionales") },
            placeholder = { Text(text = "Si hay algún dato importante que los posibles auxiliadores deban saber, menciónalo.") },
            colors = textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        CustomButton(text = "Guardar") {
            if(instructions.isNotEmpty()){
                viewModel.updateEmergencyInstructions(instructions)
                showSuccessMessage = true
            }
        }
    }
}