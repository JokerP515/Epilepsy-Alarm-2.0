package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uan.designsystem.uikit.components.UanButtonStyle
import com.uan.designsystem.uikit.components.UanCard
import com.uan.designsystem.uikit.components.UanModal
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.ui.buttons.CustomButton

@Composable
fun ContactCard(
    contact: EmergencyContactEntity,
    onUpdate: (EmergencyContactEntity) -> Unit,
    onDelete: () -> Unit,
    onSendSms: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val spacing = tokens.spacing
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded } // Para desplegar o contraer
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = colors.onSurface
        )

        UanCard(
            title = contact.name,
            body = if (expanded) "Teléfono: ${contact.phoneNumber} ${if (!contact.isConfirmed) "\nEstado: Sin confirmar" else ""}" else null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing.sm)
                .animateContentSize(),
            footer = {
                if (expanded) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                    ) {
                        if (contact.isConfirmed) {
                            CustomButton(
                                text = stringResource(R.string.modificar),
                                modifier = Modifier.weight(1f)
                            ) { showEditDialog = true }
                            CustomButton(
                                text = stringResource(R.string.eliminar),
                                modifier = Modifier.weight(1f),
                                style = UanButtonStyle.Danger
                            ) { onDelete() }
                        } else {
                            CustomButton(
                                text = stringResource(R.string.confirmar),
                                modifier = Modifier.weight(1f),
                                style = UanButtonStyle.Warning
                            ) {
                                onSendSms(contact.phoneNumber)
                                showConfirmationDialog = true
                            }
                            CustomButton(
                                text = stringResource(R.string.eliminar),
                                modifier = Modifier.weight(1f),
                                style = UanButtonStyle.Danger
                            ) { onDelete() }
                        }
                    }
                }
            }
        )
    }

    if (showEditDialog) {
        ContactDialog(
            isEditing = true,
            initialName = contact.name,
            initialPhone = contact.phoneNumber,
            onDismiss = { showEditDialog = false },
            onConfirm = { updatedContact ->
                onUpdate(
                    contact.copy(
                        name = updatedContact.name,
                        phoneNumber = updatedContact.phoneNumber
                    )
                )
                showEditDialog = false
            }
        )
    }

    if (showConfirmationDialog) {
        UanModal(
            modifier = Modifier.wrapContentHeight(),
            visible = true,
            showCloseButton = true,
            onDismissRequest = { showConfirmationDialog = false },
            title = stringResource(R.string.titulo_confirmar_contacto),
            media = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing.sm),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        text = stringResource(R.string.confirmar_contacto),
                        style = tokens.typography.body
                    )

                    Spacer(modifier = Modifier.height(spacing.xs))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                    ) {
                        CustomButton(
                            text = stringResource(R.string.aceptar),
                            modifier = Modifier.weight(1f),
                            style = UanButtonStyle.Success
                        ) {
                            onUpdate(contact.copy(isConfirmed = true))
                            showConfirmationDialog = false
                        }
                        CustomButton(
                            text = stringResource(R.string.modificar),
                            modifier = Modifier.weight(1f),
                            style = UanButtonStyle.Primary
                        ) {
                            showConfirmationDialog = false
                            showEditDialog = true
                        }
                    }
                }
            }
        )
    }
}

