package com.uan.epilepsyalarm20.ui.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.ui.cards.ClickableHeadlineCard
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes

@Composable
fun ConfigurationScreen(navController: NavHostController) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        ClickableHeadlineCard(
            title = stringResource(R.string.configura_la_alarma),
        ) {
            navController.navigate(Routes.ConfigAlarma.id)
        }

        Spacer(modifier = Modifier.height(16.dp))

        ClickableHeadlineCard(
            title = stringResource(R.string.contactos_de_emergencia),
        ) {
            navController.navigate(Routes.Contactos.id)
        }
    }
}