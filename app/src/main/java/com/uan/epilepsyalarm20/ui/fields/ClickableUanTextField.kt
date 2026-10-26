package com.uan.epilepsyalarm20.ui.fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.uan.designsystem.uikit.components.UanTextField

// fix temporal para hacer clickeable toda la linea del textField
@Composable
fun ClickableUanTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    readOnly: Boolean = false,
    placeholder: String ?= null
) {
    val focusRequester = remember { FocusRequester() }

    UanTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier
            .focusRequester(focusRequester)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusRequester.requestFocus()
            },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        readOnly = readOnly,
        placeholder = placeholder
    )
}