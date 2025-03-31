package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.theme.textFieldColors

@Composable
fun ContactDialog(
    isEditing: Boolean,
    initialName: String = "",
    initialPhone: String = "",
    onDismiss: () -> Unit,
    onConfirm: (EmergencyContactEntity) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var phone by remember { mutableStateOf(initialPhone) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) stringResource(R.string.editar_contacto) else stringResource(R.string.agregar_contacto)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.nombre)) },
                    colors = textFieldColors()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { input ->
                        if (input.all { char -> char.isDigit() }) {
                            phone = input
                        }
                    },
                    label = { Text(stringResource(R.string.telefono)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldColors()
                )
            }
        },
        confirmButton = {
            CustomButton(if (isEditing) stringResource(R.string.guardar) else stringResource(R.string.agregar)) {
                onConfirm(EmergencyContactEntity(userId = 1, name = name, phoneNumber = phone))
            }
        },
        dismissButton = {
            CustomButton(stringResource(R.string.cancelar)) { onDismiss() }
        }
    )
}
