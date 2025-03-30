package com.uan.epilepsyalarm20.ui.screens.mainMenu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uan.epilepsyalarm20.domain.models.ContactsViewModel
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.ContactCard
import com.uan.epilepsyalarm20.ui.cards.ContactDialog
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import kotlinx.coroutines.launch

@Composable
fun ContactsScreen(contactsViewModel: ContactsViewModel, navController: NavHostController) {
    val listContacts = contactsViewModel.emergencyContactsFlow.collectAsState(initial = emptyList())
    val currentCount by contactsViewModel.currentCount.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    var showErrorMessage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    BackHandler {
        navController.navigate(Routes.Informacion.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadlineCard("Contactos de Emergencia")

        CustomButton("Agregar Contacto") {
            if (currentCount < contactsViewModel.limit) {
                showDialog = true
            }else showErrorMessage = true
        }

        if (showDialog) {
            ContactDialog(
                isEditing = false,
                onConfirm = { contact ->
                    contactsViewModel.insertEmergencyContact(contact.name, contact.phoneNumber)
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
