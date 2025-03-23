package com.uan.epilepsyalarm20.ui.screens.mainMenu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.domain.models.ContactsViewModel
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.cards.ContactCard
import com.uan.epilepsyalarm20.ui.cards.ContactDialog
import com.uan.epilepsyalarm20.ui.cards.HeadlineCard

@Composable
fun ContactsScreen(contactsViewModel: ContactsViewModel) {
    val listContacts = contactsViewModel.emergencyContactsFlow.collectAsState(initial = emptyList())
    val currentCount by contactsViewModel.currentCount.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

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
            }
            Log.d("ContactsScreen", "Current Count: $currentCount")
            Log.d("ContactsScreen", "Limit: ${contactsViewModel.limit}")
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
}
