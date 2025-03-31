package com.uan.epilepsyalarm20.ui.screens.mainMenu

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard

@Composable
fun InformationScreen() {

    val context = LocalContext.current

    BackHandler {
        val activity = context as? Activity
        activity?.moveTaskToBack(true) // Mueve la app al fondo sin cerrarla
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadlineCard(
            title = stringResource(R.string.inicio),
            description = stringResource(R.string.explicacion_pantalla_inicio)
        )
        HeadlineCard(
            title = stringResource(R.string.configuracion),
            description = stringResource(R.string.explicacion_pantalla_configuracion)
        )
        HeadlineCard(
            title = stringResource(R.string.mi_perfil),
            description = stringResource(R.string.explicacion_pantalla_mi_perfil)
        )
        HeadlineCard(
            title = stringResource(R.string.contactos),
            description = stringResource(R.string.explicacion_pantalla_contactos)
        )
    }
}