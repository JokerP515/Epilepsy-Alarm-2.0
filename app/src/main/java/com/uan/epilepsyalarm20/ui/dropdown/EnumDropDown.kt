package com.uan.epilepsyalarm20.ui.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.uan.designsystem.uikit.components.UanDropdownMenu
import com.uan.designsystem.uikit.components.UanDropdownMenuItem
import com.uan.designsystem.uikit.components.UanTextField

@Composable
fun <T : Enum<T>> EnumDropdown(
    selectedOption: T?,
    options: List<T>,
    label: String,
    placeholder: String,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val menuItems = remember(options) {
        options.map { item ->
            UanDropdownMenuItem(
                label = item.toString(),
                contentDescription = "Seleccionar ${item.name}",
                onClick = {
                    onOptionSelected(item)
                    expanded = false
                }
            )
        }
    }

    Box(modifier = modifier) {
        UanTextField(
            value = selectedOption?.toString() ?: "",
            onValueChange = {},
            label = label,
            placeholder = placeholder,
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = if (expanded) "Cerrar menú" else "Abrir menú"
                )
            }
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    expanded = !expanded
                }
        )

        if (expanded) {
            Popup(
                properties = PopupProperties(focusable = true),
                onDismissRequest = { expanded = false }
            ) {
                UanDropdownMenu(
                    expanded = true,
                    items = menuItems,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}