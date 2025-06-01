package com.uan.epilepsyalarm20.ui.screens.menu

import android.app.Activity
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.theme.imageSize

@Composable
fun ExplicationScreen(navController: NavHostController? = null, go: (Any) -> Unit = {}, boolean: Boolean = false){

    val context = LocalContext.current

    BackHandler {
        if(!boolean) {
            val activity = context as? Activity
            activity?.moveTaskToBack(true) // Mueve la app al fondo sin cerrarla
        }else {
            navController?.navigateUp()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .then(
                if (!boolean) {
                    Modifier
                        .padding(
                            top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding(),
                            bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
                        )
                }
                else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(31.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeadlineCard(
            title = stringResource(R.string.configura_la_alarma)
        )

        Image(
            painter = painterResource(R.drawable.holding_phone),
            contentDescription = "Sosteniendo teléfono",
            modifier = Modifier.size(imageSize())
        )

        BasicText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append("El botón de ")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primaryContainer)) {
                    append("encendido")
                }
                withStyle (style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append(" y ")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primaryContainer)) {
                    append("apagado")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append(", será el activador de la alarma.")
                }
            },
            style = MaterialTheme.typography.titleLarge.copy(
                textAlign = TextAlign.Center
            ),
        )

//        Text(
//            text = stringResource(R.string.Mensaje_De_Explicacion),
//            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorScheme.onBackground,
//            textAlign = TextAlign.Center
//        )

        CustomButton(text = stringResource(R.string.siguiente)) {
            if(navController != null) {
                navController.navigate(Routes.ConfigActivacionAlarma.id)
            } else {
                go(Routes.ConfigActivacionAlarma)
            }
        }
    }
}