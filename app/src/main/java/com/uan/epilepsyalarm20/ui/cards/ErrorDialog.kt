package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.uan.designsystem.uikit.components.UanModal
import com.uan.designsystem.uikit.components.UanModalAction
import com.uan.designsystem.uikit.components.UanButtonStyle
import com.uan.epilepsyalarm20.R

@Composable
fun ErrorDialog(
    errorMessages: List<String>,
    onDismiss: () -> Unit
) {
    val message = errorMessages.joinToString(separator = "\n") { "• $it" }

    UanModal(
        visible = true,
        onDismissRequest = onDismiss,
        title = stringResource(R.string.falta_la_siguiente_informacion),
        body = message,
        primaryAction = UanModalAction(
            label = "Aceptar",
            onClick = onDismiss,
            style = UanButtonStyle.Primary
        )
    )
}