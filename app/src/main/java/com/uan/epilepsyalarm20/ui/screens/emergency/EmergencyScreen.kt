package com.uan.epilepsyalarm20.ui.screens.emergency

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.uan.designsystem.uikit.components.UanAppBar
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.data.local.entities.UserEntity
import com.uan.epilepsyalarm20.domain.models.EmergencyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun EmergencyScreen(
    viewModel: EmergencyViewModel
) {
    val context = LocalContext.current
    val countdown by viewModel.countdown.collectAsState()
    val isEmergencyActive by viewModel.isEmergencyActive.collectAsState()

    var user by remember { mutableStateOf<UserEntity?>(null) }
    var firstEmergencyContact by remember { mutableStateOf<EmergencyContactEntity?>(null) }

    val hasStartedCountdown by remember { derivedStateOf { isEmergencyActive || countdown < 5 } }

    LaunchedEffect(Unit)  {
        if (!hasStartedCountdown) {
            viewModel.startEmergencyCountdown()
        }
        user = viewModel.getUser()

        val contactConfirmed = viewModel.getFirstConfirmedContact()
        firstEmergencyContact = contactConfirmed?: viewModel.getFirstEmergencyContact()
    }

    BackHandler {
        viewModel.cancelEmergency()
        val activity = context as? Activity
        activity?.moveTaskToBack(true)
    }

    Scaffold (
        topBar = {
            UanAppBar(
                title = stringResource(R.string.epilepsy_alarm),
                modifier = Modifier.padding(
                    top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
                )
            )
        }
    ) {
        innerPadding ->
        if(isEmergencyActive) {
            user?.let { EmergencyContent(innerPadding, viewModel, context, it, firstEmergencyContact) }
        } else {
            EmergencyCountDownContent(innerPadding, viewModel, context, countdown)
        }
    }
}