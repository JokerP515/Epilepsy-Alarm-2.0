package com.uan.epilepsyalarm20.ui.screens.mainMenu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.domain.models.StartViewModel
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.theme.textFieldColors
import kotlinx.coroutines.launch

@Composable
fun StartScreen(viewModel: StartViewModel, navController: NavHostController) {
    var message by rememberSaveable { mutableStateOf("") }
    var instructions by rememberSaveable { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessMessage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    BackHandler {
        navController.navigate(Routes.Informacion.id)
    }

    LaunchedEffect(Unit) {
        message = viewModel.getEmergencyMessage() ?: ""
        instructions = viewModel.getEmergencyInstructions() ?: ""
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        HeadlineCard(
            title = stringResource(R.string.configura_mensaje_instrucciones),
            description = stringResource(R.string.explicacion_pantalla_inicio_mensajes)
        )

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text(stringResource(R.string.mensaje_de_alerta)) },
            placeholder = { Text(text = stringResource(R.string.explicacion_mensaje_alerta)) },
            colors = textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        CustomButton(text = stringResource(R.string.guardar)) {
            if(message.isNotEmpty()){
                viewModel.updateEmergencyMessage(message)
                showSuccessMessage = true
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = instructions,
            onValueChange = { instructions = it },
            label = { Text(stringResource(R.string.instrucciones_o_datos_adicionales)) },
            placeholder = { Text(text = stringResource(R.string.explicacion_instrucciones_o_datos_adicionales)) },
            colors = textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        CustomButton(text = stringResource(R.string.guardar)) {
            if(instructions.isNotEmpty()){
                viewModel.updateEmergencyInstructions(instructions)
                showSuccessMessage = true
            }
        }
    }

    if (showSuccessMessage) {
        LaunchedEffect(true) {
            // Mostrar Snackbar utilizando SnackbarHostState
            scope.launch {
                snackbarHostState.showSnackbar("¡Información editada con éxito!")
            }
            showSuccessMessage = false // Ocultar el mensaje después de mostrarlo
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}