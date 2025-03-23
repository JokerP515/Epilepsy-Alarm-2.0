package com.uan.epilepsyalarm20.ui.screens.mainMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes

//@Preview(showBackground = true)
@Composable
fun ExplicationScreen(navController: NavHostController? = null, go: (Any) -> Unit = {}){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeadlineCard(title = stringResource(R.string.configura_la_alarma))

        // Placeholder para la imagen del Recurso 1 1 FIGMA
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Imagen pendiente, NOOOOOOOOOOOOO lo olvide")
        }

        Text(text = stringResource(R.string.Mensaje_De_Explicacion),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
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