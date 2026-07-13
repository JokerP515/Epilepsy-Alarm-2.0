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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.domain.models.MainViewModel
import com.uan.epilepsyalarm20.domain.models.SoundSelectViewModel
import com.uan.epilepsyalarm20.domain.models.Sounds
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.ErrorDialog
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.dropdown.EnumDropdown
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.theme.imageSize
import kotlinx.coroutines.launch

@Composable
fun SoundSelectScreen(
    soundSelectViewModel: SoundSelectViewModel,
    mainViewModel: MainViewModel,
    navController: NavHostController? = null,
    go: (Any) -> Unit = {},
    boolean: Boolean = false
) {

    DisposableEffect(Unit) {
        onDispose {
            soundSelectViewModel.stopAudio()
        }
    }

    BackHandler {
        if(navController != null) {
            navController.navigateUp()
        } else {
            go(Routes.ConfigAlarma)
        }
    }

    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val typography = tokens.typography
    val spacing = tokens.spacing
    val sounds = Sounds.entries
    var selectedSound by rememberSaveable { mutableStateOf(sounds.first()) }
    val isPlaying by soundSelectViewModel.isPlaying.collectAsState()
    var showProblem by rememberSaveable { mutableStateOf(false) }
    val list: List<String> = listOf("Selecciona un sonido para continuar")


    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessMessage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val savedSound = soundSelectViewModel.getSoundPreference()
        if (savedSound != null) {
            selectedSound = Sounds.toEnumSounds(savedSound)
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
                            bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
                        )
                }
                else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadlineCard(
            title = stringResource(R.string.configura_la_alarma),
        )

        BasicText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colors.onSurface)) {
                    append("Selecciona el ")
                }
                withStyle(style = SpanStyle(color = colors.primary)) {
                    append("sonido")
                }
                withStyle (style = SpanStyle(color = colors.onSurface)) {
                    append(" de la ")
                }
                withStyle(style = SpanStyle(color = colors.primary)) {
                    append("alarma")
                }
                withStyle(style = SpanStyle(color = colors.onSurface)) {
                    append(".")
                }
            },
            style = typography.component.copy(
                textAlign = TextAlign.Center
            ),
        )

        EnumDropdown(
            selectedOption=selectedSound,
            options=sounds,
            label= stringResource(R.string.alarma),
            placeholder= stringResource(R.string.seleccionar_alarma),
            onOptionSelected= {selectedSound = it}
        )

        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = if(isPlaying) stringResource(R.string.detener_alarma_seleccionada) else stringResource(
            R.string.reproducir_alarma_seleccionada
        )) {
            soundSelectViewModel.togglePlayStop(selectedSound)
        }

        Image(
            painter = painterResource(R.drawable.bell_adjusted),
            contentDescription = stringResource(R.string.alarma),
            modifier = Modifier.size(imageSize())
        )

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Posible Color inverso
            CustomButton(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.atras)
            ){
                if(navController != null) {
                    navController.navigateUp()
                } else {
                    go(Routes.ConfigAlarma)
                }
            }

            // Posible Color inverso
            CustomButton(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.finalizar),
            ){
                if(isSoundSelected(selectedSound)){
                    soundSelectViewModel.stopAudio()
                    soundSelectViewModel.saveSoundPreference(selectedSound.getFileName())
                    mainViewModel.setInitialConfigCompleted()
                    if(navController != null) {
                        showSuccessMessage = true
                    } else {
                        go(Routes.MenuPrincipal)
                    }
                }else {
                    showProblem = true
                }
            }
        }

        if (showProblem) {
            ErrorDialog(
                onDismiss = { showProblem = false },
                errorMessages = list
            )
        }
    }

    if (showSuccessMessage) {
        LaunchedEffect(true) {

            scope.launch {
                snackbarHostState.showSnackbar("¡Información editada con éxito!")
            }
            showSuccessMessage = false // Ocultar el mensaje después de mostrarlo
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}

fun isSoundSelected(sound: Sounds): Boolean = sound != Sounds.SELECTOPTION

