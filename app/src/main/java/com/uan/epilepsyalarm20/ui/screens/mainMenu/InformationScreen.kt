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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard

@Composable
fun InformationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
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