package com.uan.epilepsyalarm20.ui.screens.mainMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.epilepsyalarm20.domain.models.ActivationMethod
import com.uan.epilepsyalarm20.domain.models.ActivationMethodViewModel
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.dropdown.EnumDropdown
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes

@Composable
fun ActivationMethodScreen(
    activationMethodViewModel: ActivationMethodViewModel,
    navController: NavHostController? = null,
    go: (Any) -> Unit = {}) {

    val activationMethods = ActivationMethod.entries
    var selectedActivationMethod by rememberSaveable { mutableStateOf(activationMethods.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadlineCard(
            title = "Configura la Alarma",
        )

        Text(
            "Selecciona la cantidad de veces que deberás presionar el botón de encendido y apagado para la activación de la alarma.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        HeadlineCard(
            title = "Recuerda",
            description = "Recuerda que la pulsación del botón de encendido debe ser realizada en un plazo máximo de 5 segundos"
        )

        EnumDropdown(
            selectedOption = selectedActivationMethod,
            options = activationMethods,
            label = "Método de activación",
            placeholder = "Seleccionar método de activación",
            onOptionSelected = { selectedActivationMethod = it }
        )

        CustomButton(text = "Guardar Opción") {
            activationMethodViewModel.saveActivationMethod(selectedActivationMethod.getValue())
            if(navController != null) {
                navController.navigate(Routes.ConfigSonidoAlarma.id)
            } else {
                go(Routes.ConfigSonidoAlarma)
            }
        }
    }
}