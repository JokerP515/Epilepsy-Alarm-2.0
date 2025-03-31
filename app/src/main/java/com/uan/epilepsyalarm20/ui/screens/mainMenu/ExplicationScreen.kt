package com.uan.epilepsyalarm20.ui.screens.mainMenu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.theme.imageSize

@Composable
fun ExplicationScreen(navController: NavHostController? = null, go: (Any) -> Unit = {}, boolean: Boolean = false){

    BackHandler {
        navController?.navigate(Routes.Informacion.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .then(
                if(!boolean) Modifier.padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
                else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeadlineCard(
            title = stringResource(R.string.configura_la_alarma),
            description = stringResource(R.string.Mensaje_De_Explicacion)
        )

        Image(
            painter = painterResource(R.drawable.dispositivo_activacion),
            contentDescription = "Dispositivo Activación",
            modifier = Modifier.size(imageSize())
        )

        CustomButton(text = stringResource(R.string.siguiente)) {
            if(navController != null) {
                navController.navigate(Routes.ConfigActivacionAlarma.id)
            } else {
                go(Routes.ConfigActivacionAlarma)
            }
        }
    }
}