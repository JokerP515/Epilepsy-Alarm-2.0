package com.uan.epilepsyalarm20.ui.screens.mainMenu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.domain.models.ActivationMethod
import com.uan.epilepsyalarm20.domain.models.ActivationMethod.Companion.toEnumActivationMethod
import com.uan.epilepsyalarm20.domain.models.ActivationMethodViewModel
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.dropdown.EnumDropdown
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.theme.imageSize

@Composable
fun ActivationMethodScreen(
    activationMethodViewModel: ActivationMethodViewModel,
    navController: NavHostController? = null,
    go: (Any) -> Unit = {},
    boolean: Boolean = false
) {

    BackHandler {
        if(navController != null) {
            navController.navigateUp()
        } else {
            go(Routes.ConfigAlarma)
        }
    }

    val activationMethods = ActivationMethod.entries
    var selectedActivationMethod by rememberSaveable { mutableStateOf(activationMethods.first()) }

    LaunchedEffect(Unit) {
        selectedActivationMethod =
            toEnumActivationMethod(activationMethodViewModel.getActivationMethod())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .then(
                if (!boolean) Modifier.padding(
                    top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
                )
                else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadlineCard(
            title = stringResource(R.string.configura_la_alarma),
            description = stringResource(R.string.explicacion_activacion_alarma)
        )

        Image(
            painter = painterResource(R.drawable.recurso1_2),
            contentDescription = "Dispositivo Activación",
            modifier = Modifier.size(imageSize())
        )

        HeadlineCard(
            title = stringResource(R.string.recuerda),
            description = stringResource(R.string.info_adicional_plazo_activacion)
        )

        EnumDropdown(
            selectedOption = selectedActivationMethod,
            options = activationMethods,
            label = stringResource(R.string.metodo_de_activacion),
            placeholder = stringResource(R.string.seleccionar_metodo_de_activacion),
            onOptionSelected = { selectedActivationMethod = it }
        )

        CustomButton(text = stringResource(R.string.guardar_opcion)) {
            activationMethodViewModel.saveActivationMethod(selectedActivationMethod.getValue())
            if(navController != null) {
                navController.navigate(Routes.ConfigSonidoAlarma.id)
            } else {
                go(Routes.ConfigSonidoAlarma)
            }
        }
    }
}