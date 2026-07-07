package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.uan.designsystem.uikit.components.UanButton
// Importaciones del UAN Design System
import com.uan.designsystem.uikit.components.UanModal
import com.uan.designsystem.uikit.components.UanModalAction
import com.uan.designsystem.uikit.components.UanButtonStyle
import com.uan.designsystem.uikit.components.UanTextField
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.ui.buttons.CustomButton
import com.uan.epilepsyalarm20.ui.fields.ClickableUanTextField

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

    val tokens = UanThemeTokens.current
    val spacing = tokens.spacing

    UanModal(
        modifier = Modifier.wrapContentHeight(),
        visible = true,
        onDismissRequest = onDismiss,
        title = if (isEditing) stringResource(R.string.editar_contacto) else stringResource(R.string.agregar_contacto),
        media = {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
            ) {
                ClickableUanTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = stringResource(R.string.nombre),
                    modifier = Modifier.fillMaxWidth()
                )

                ClickableUanTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = stringResource(R.string.telefono),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )

                Spacer(modifier = Modifier.height(spacing.xs))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                ) {
                    CustomButton(
                        text = stringResource(R.string.guardar),
                        modifier = Modifier.weight(1f)
                    ) {
                        onConfirm(EmergencyContactEntity(name = name, phoneNumber = phone, userId = 1))
                    }

                    CustomButton(
                        text = stringResource(R.string.cancelar),
                        modifier = Modifier.weight(1f)
                    ) {
                        onDismiss()
                    }

                }
            }
        }
    )
}