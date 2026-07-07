package com.uan.epilepsyalarm20.ui.screens.menu

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uan.designsystem.uikit.components.UanCardDefaults
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.ui.theme.imageSize
import com.uan.epilepsyalarm20.utils.EmergencyLauncher

@Composable
fun StartScreen() {
    val context = LocalContext.current
    val tokens = UanThemeTokens.current
    val colors = tokens.colors

    BackHandler {
        val activity = context as? Activity
        activity?.moveTaskToBack(true) // Mueve la app al fondo sin cerrarla
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.informacion_inicio_activacion),
            style = UanCardDefaults.titleStyle,
            textAlign = TextAlign.Center,
            color = colors.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(R.drawable.start_screen_logo),
            contentDescription = stringResource(R.string.logo_epilepsy_alarm),
            modifier = Modifier
                .size(imageSize())
                .clickable {
                    EmergencyLauncher.launch(context)
                }
        )
    }
}