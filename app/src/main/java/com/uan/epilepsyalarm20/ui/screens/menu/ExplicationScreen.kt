package com.uan.epilepsyalarm20.ui.screens.menu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.uan.designsystem.uikit.components.UanButtonStyle
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.domain.models.StartViewModel
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.fields.ClickableUanTextField
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.theme.imageSize
import kotlinx.coroutines.launch

@Composable
fun ExplicationScreen(navController: NavHostController? = null, go: (Any) -> Unit = {}, boolean: Boolean = false, viewModel: StartViewModel = hiltViewModel()){

    val context = LocalContext.current
    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val spacing = tokens.spacing
    val typography = tokens.typography

    var message by rememberSaveable { mutableStateOf("") }
    var instructions by rememberSaveable { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessMessage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        message = viewModel.getEmergencyMessage() ?: ""
        instructions = viewModel.getEmergencyInstructions() ?: ""
    }

    BackHandler {
        if(!boolean) {
            go(Routes.PerfilUsuario)
        }else {
            navController?.navigateUp()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .then(
                if (!boolean) {
                    Modifier
                        .padding(
                            top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding(),
                            bottom = WindowInsets.systemBars.asPaddingValues()
                                .calculateBottomPadding()
                        )
                } else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeadlineCard(
            title = stringResource(R.string.configura_la_alarma)
        )

        BasicText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors.onSurface)) {
                    append("El botón de ")
                }
                withStyle(style = SpanStyle(color = colors.primary)) {
                    append("subir volumen")
                }
                withStyle(style = SpanStyle(color = colors.onSurface)) {
                    append(", será el activador de la alarma.")
                }
            },
            style = typography.general.copy(
                textAlign = TextAlign.Center
            ),
        )

        BasicText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors.onSurface)) {
                    append("Debes mantener presionado el botón de ")
                }
                withStyle(style = SpanStyle(color = colors.primary)) {
                    append("subir volumen")
                }
                withStyle(style = SpanStyle(color = colors.onSurface)) {
                    append(" para poder activar la alarma.")
                }
            },
            style = typography.general.copy(
                textAlign = TextAlign.Center
            ),
        )

        Image(
            painter = painterResource(R.drawable.holding_phone_adjusted),
            contentDescription = stringResource(R.string.sosteniendo_telefono),
            modifier = Modifier.size(imageSize())
        )

        BasicText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors.onSurface)) {
                    append("Escribe un ")
                }
                withStyle(style = SpanStyle(color = colors.primary)) {
                    append("mensaje personalizado ")
                }
                withStyle (style = SpanStyle(color = colors.onSurface)) {
                    append("que se enviará al activar la alarma e incluye ")
                }
                withStyle(style = SpanStyle(color = colors.primary)) {
                    append("instrucciones ")
                }
                withStyle(style = SpanStyle(color = colors.onSurface)) {
                    append("breves para el manejo de la crisis.")
                }
            },
            style = typography.general.copy(
                textAlign = TextAlign.Center
            ),
        )

        // Mensajes de configuración de alarma
        ClickableUanTextField(
            value = message,
            onValueChange = { message = it},
            label = stringResource(R.string.mensaje_de_alerta),
            placeholder = stringResource(R.string.explicacion_mensaje_alerta),
            modifier = Modifier.fillMaxWidth()
        )

        ClickableUanTextField(
            value = instructions,
            onValueChange = { instructions = it },
            label = stringResource(R.string.instrucciones_o_datos_adicionales),
            placeholder = stringResource(R.string.explicacion_instrucciones_o_datos_adicionales),
            modifier = Modifier.fillMaxWidth()
        )

        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            // Posible Color inverso
            CustomButton(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.guardar),
                style = UanButtonStyle.Secondary
            ){
                if(message.isNotEmpty()){
                    viewModel.updateEmergencyMessage(message)
                }
                if(instructions.isNotEmpty()){
                    viewModel.updateEmergencyInstructions(instructions)
                }
                showSuccessMessage = instructions.isNotEmpty() || message.isNotEmpty()
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Posible Color inverso
            CustomButton(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.atras),
            ){
                if(!boolean) {
                    go(Routes.PerfilUsuario)
                }else {
                    navController?.navigateUp()
                }
            }

            // Posible Color inverso
            CustomButton(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.siguiente)
            ){
                if(navController != null) {
                    navController.navigate(Routes.ConfigSonidoAlarma.id)
                } else {
                    go(Routes.ConfigSonidoAlarma)
                }
            }
        }
    }

    if (showSuccessMessage) {
        LaunchedEffect(true) {
            // Mostrar Snackbar utilizando SnackbarHostState
            scope.launch {
                snackbarHostState.showSnackbar("¡Información editada con éxito!")
            }
            showSuccessMessage = false // Ocultar el mensaje después de mostrarlo
        }
    }
    SnackbarHost(hostState = snackbarHostState)
}