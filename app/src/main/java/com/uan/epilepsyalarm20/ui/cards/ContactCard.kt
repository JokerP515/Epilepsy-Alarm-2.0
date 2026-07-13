package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
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
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.ui.buttons.CustomButton

@Composable
fun ContactCard(
    contact: EmergencyContactEntity,
    onUpdate: (EmergencyContactEntity) -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

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
            body = if (expanded) "Teléfono: ${contact.phoneNumber}" else null,
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
                        CustomButton(
                            text = stringResource(R.string.modificar),
                            modifier = Modifier.weight(1f),
                        ){
                            showDialog = true
                        }
                        CustomButton(
                            text = stringResource(R.string.eliminar),
                            modifier = Modifier.weight(1f),
                            style = UanButtonStyle.Danger,
                        ){ onDelete() }
                    }
                }
            }
        )
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

