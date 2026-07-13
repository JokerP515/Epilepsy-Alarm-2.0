package com.uan.epilepsyalarm20.ui.screens.menu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.designsystem.uikit.components.UanButton
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.domain.models.ContactsViewModel
import com.uan.epilepsyalarm20.ui.cards.ErrorDialog
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.fields.ClickableUanTextField

@Composable
fun NewContactScreen(contactsViewModel: ContactsViewModel, navController: NavHostController) {

    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val spacing = tokens.spacing
    val typography = tokens.typography

    var name by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }

    var errorMessages by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    BackHandler {
        navController.navigateUp()
    }

    if (showDialog) {
        ErrorDialog(errorMessages = errorMessages, onDismiss = { showDialog = false })
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadlineCard(stringResource(R.string.nuevo_contacto_emergencia))

            ClickableUanTextField(
                value = name,
                onValueChange = { name = it },
                label = stringResource(R.string.nombre_del_contacto_de_emergencia),
                modifier = Modifier.fillMaxWidth()
            )

            ClickableUanTextField(
                value = phoneNumber,
                onValueChange = { input ->
                    if (input.all { char -> char.isDigit() }) {
                        phoneNumber = input
                    }
                },
                label = stringResource(R.string.numero_del_contacto_de_emergencia),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

        UanButton (
            onClick = {
                val missingFields = checkMissingFields(name, phoneNumber)
                if (missingFields.isEmpty()) {
                    contactsViewModel.insertEmergencyContact(name, phoneNumber)
                    navController.navigateUp()
                } else {
                    errorMessages = missingFields
                    showDialog = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.guardar),
                style = typography.general
            )
        }
    }
}

fun checkMissingFields (name: String, phoneNumber: String): List<String> {
    val missing = mutableListOf<String>()
    if(name.isBlank()) missing.add("Nombre del Contacto de Emergencia")
    if(phoneNumber.isBlank()) missing.add("Número del Contacto de Emergencia")
    return missing
}