package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.ui.theme.defaultCardColors

@Composable
fun ContactCard(
    contact: EmergencyContactEntity,
    onDelete: () -> Unit,
    onUpdate: (EmergencyContactEntity) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = defaultCardColors(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Nombre: ${contact.name}",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Teléfono: ${contact.phoneNumber}",
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button (
                    onClick = { showDialog = true },
                    modifier = Modifier.weight(1f)
                ){
                    Text(
                        text = stringResource(R.string.modificar),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button (
                    onClick = { onDelete() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ){
                    Text(
                        text = stringResource(R.string.eliminar),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }

    if (showDialog) {
        ContactDialog(
            isEditing = true,
            initialName = contact.name,
            initialPhone = contact.phoneNumber,
            onDismiss = { showDialog = false },
            onConfirm = { updatedContact ->
                onUpdate(contact.copy(name = updatedContact.name, phoneNumber = updatedContact.phoneNumber))
                showDialog = false
            }
        )
    }
}

