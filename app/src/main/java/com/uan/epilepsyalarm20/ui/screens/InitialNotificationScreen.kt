package com.uan.epilepsyalarm20.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uan.designsystem.uikit.components.UanModal
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.domain.models.InitialNotificationViewModel
import com.uan.epilepsyalarm20.domain.models.MainViewModel
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.TermsAndConditions
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.theme.imageSize

@Composable
fun InitialNotificationScreen(
    initialNotificationViewModel: InitialNotificationViewModel,
    mainViewModel: MainViewModel,
    go: (Any) -> Unit
) {
    var termsAccepted by rememberSaveable { mutableStateOf(false) }
    var showTermsUanModel by rememberSaveable { mutableStateOf(false) }

    val userExistsState by mainViewModel.userExists.collectAsState(initial = null)
    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val spacing = tokens.spacing
    val typography = tokens.typography

    // Determinar la ruta de navegación
    val nextDestination = when (userExistsState) {
        null -> null // Muestra pantalla de carga
        true -> Routes.MenuPrincipal
        else -> Routes.PerfilUsuario
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .padding(
                top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
            ),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.titulo_funcionamiento_de_la_aplicacion_epilepsy_alarm),
            style = typography.component,
            color = colors.primary,
            textAlign = TextAlign.Center,
            fontWeight = W700
        )

        Spacer(modifier = Modifier.height(spacing.md))

        Text(
            text = stringResource(R.string.Explicacion_Funcionamiento_EPAlarm),
            style = typography.general,
            color = colors.onSurface,
            fontWeight = W400
        )

        Image(
            painter = painterResource(R.drawable.standup),
            contentDescription = stringResource(R.string.persona_ayudando),
            modifier = Modifier.size(imageSize())
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { showTermsUanModel = true }
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        if (termsAccepted) colors.primary else colors.onSurface.copy(alpha = 0.5f),
                        CircleShape
                    )
            )
            Text(
                text = stringResource(R.string.aceptar_terminos_condiciones),
                modifier = Modifier.padding(start = spacing.sm),
                style = typography.general,
                color = colors.primary
            )
        }

        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.iniciar),
            enabled = termsAccepted
        ) {
            if (termsAccepted) initialNotificationViewModel.notShowAgain()
            if (nextDestination != null) go(nextDestination)
        }
    }

    if (showTermsUanModel) {
        UanModal(
            visible = true,
            onDismissRequest = { showTermsUanModel = false },
            title = stringResource(R.string.titulo_terminos_condiciones),
            showCloseButton = true,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.6f)
                .padding(horizontal = spacing.md)
        ) {
            TermsAndConditions(
                onAccept = {
                    termsAccepted = true
                    showTermsUanModel = false
                },
                onDismiss = {
                    termsAccepted = false
                    showTermsUanModel = false
                }
            )
        }
    }
}
