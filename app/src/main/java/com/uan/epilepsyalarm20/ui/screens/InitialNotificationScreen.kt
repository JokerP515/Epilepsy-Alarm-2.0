package com.uan.epilepsyalarm20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.domain.models.InitialNotificationViewModel
import com.uan.epilepsyalarm20.domain.models.MainViewModel
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes

@Composable
fun InitialNotificationScreen(
    initialNotificationViewModel: InitialNotificationViewModel,
    mainViewModel: MainViewModel,
    go: (Any) -> Unit
) {
    var isChecked by rememberSaveable { mutableStateOf(false) }
    val userExistsState by mainViewModel.userExists.collectAsState(initial = null)

    // Determinar la ruta de navegación
    val nextDestination = when (userExistsState) {
        null -> null // Muestra pantalla de carga
        true ->
            if (mainViewModel.isEmergencyMethodSaved) Routes.MenuPrincipal
            else Routes.ConfigAlarma
        else -> Routes.PerfilUsuario
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.titulo_funcionamiento_de_la_aplicacion_epilepsy_alarm),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.Explicacion_Funcionamiento_EPAlarm),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { isChecked = !isChecked }
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Text(
                text = stringResource(R.string.no_volver_a_mostrar_este_mensaje),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        CustomButton(text = stringResource(R.string.siguiente)) {
            if (isChecked) initialNotificationViewModel.notShowAgain()
            if (nextDestination != null) go(nextDestination)
        }
    }
}
