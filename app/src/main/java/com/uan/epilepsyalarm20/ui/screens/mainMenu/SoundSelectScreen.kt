package com.uan.epilepsyalarm20.ui.screens.mainMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.domain.models.MainViewModel
import com.uan.epilepsyalarm20.domain.models.Sounds
import com.uan.epilepsyalarm20.domain.models.SoundSelectViewModel
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.ErrorDialog
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.cards.SuccessSnackbar
import com.uan.epilepsyalarm20.ui.dropdown.EnumDropdown
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes

@Composable
fun SoundSelectScreen(
    soundSelectViewModel: SoundSelectViewModel,
    mainViewModel: MainViewModel,
    navController: NavHostController? = null,
    go: (Any) -> Unit = {}
) {

    val sounds = Sounds.entries
    var selectedSound by rememberSaveable { mutableStateOf(sounds.first()) }
    val isPlaying by soundSelectViewModel.isPlaying.collectAsState()
    var showProblem by rememberSaveable { mutableStateOf(false) }
    val list: List<String> = listOf("Selecciona un sonido para continuar")
    var showSuccessMessage by remember { mutableStateOf(false) }

    SuccessSnackbar(
        showMessage = showSuccessMessage,
        onDismiss = { showSuccessMessage = false }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .padding(top=WindowInsets.systemBars.asPaddingValues().calculateTopPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadlineCard(title = stringResource(R.string.configura_la_alarma))

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selecciona el sonido de la alarma.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        EnumDropdown(
            selectedOption=selectedSound,
            options=sounds,
            label="Alarma",
            placeholder="Seleccionar alarma",
            onOptionSelected= {selectedSound = it}
        )

        CustomButton(text = if(isPlaying) "Detener alarma seleccionada" else "Reproducir alarma seleccionada") {
            soundSelectViewModel.togglePlayStop(selectedSound)
        }

        Spacer(modifier = Modifier.height(16.dp))


        CustomButton (text = "Guardar Selección y Finalizar") {
            if(isSoundSelected(selectedSound)){
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

        if (showProblem) {
            ErrorDialog(
                onDismiss = { showProblem = false },
                errorMessages = list
            )
        }
    }
}

fun isSoundSelected(sound: Sounds): Boolean = sound != Sounds.SELECTOPTION

