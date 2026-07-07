package com.uan.epilepsyalarm20.ui.screens.menu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.designsystem.uikit.foundation.UanIconButton
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.domain.models.ContactsViewModel
import com.uan.epilepsyalarm20.ui.cards.ContactCard
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import kotlinx.coroutines.launch

@Composable
fun ContactsScreen(contactsViewModel: ContactsViewModel, navController: NavHostController) {
    val listContacts = contactsViewModel.emergencyContactsFlow.collectAsState(initial = emptyList())
    val currentCount by contactsViewModel.currentCount.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var showErrorMessage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val spacing = tokens.spacing

    BackHandler {
        navController.navigate(Routes.Config.id)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.md),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadlineCard(stringResource(R.string.contactos_de_emergencia))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.sm)
            ) {
                items(listContacts.value.size) { index ->
                    val contact = listContacts.value[index]
                    ContactCard(
                        contact = contact,
                        onDelete = { contactsViewModel.deleteEmergencyContact(contact) },
                        onUpdate = { updatedContact ->
                            contactsViewModel.updateEmergencyContact(updatedContact)
                        }
                    )
                }
            }
        }

        // FAB flotando abajo a la derecha
        UanIconButton(
            contentDescription = "Agregar nuevo contacto",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(spacing.sm)
                .size(56.dp),
            onClick = {
                if (currentCount < contactsViewModel.limit) {
                    navController.navigate(Routes.NuevoContacto.id)
                } else showErrorMessage = true
            },
            containerColor = colors.primary,
            shape = RoundedCornerShape(spacing.sm)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = colors.onSurface
            )
        }
    }


    if (showErrorMessage) {
        LaunchedEffect(true) {
            // Mostrar Snackbar utilizando SnackbarHostState
            scope.launch {
                snackbarHostState.showSnackbar("¡No puedes agregar más contactos!")
            }
            showErrorMessage = false // Ocultar el mensaje después de mostrarlo
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}
