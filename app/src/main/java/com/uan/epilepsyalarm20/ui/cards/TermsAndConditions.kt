package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.uan.designsystem.uikit.components.UanButtonStyle
import com.uan.designsystem.uikit.theme.UanThemeTokens
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.ui.buttons.CustomButton

@Composable
fun TermsAndConditions(onAccept: () -> Unit, onDismiss: () -> Unit) {
    val scrollState = rememberScrollState()
    val isAtBottom = scrollState.value == scrollState.maxValue

    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val spacing = tokens.spacing
    val typography = tokens.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(R.string.terminos_y_condiciones),
                style = typography.general,
                color = colors.onSurface
            )
        }

        Spacer(modifier = Modifier.padding(spacing.md))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.sm)
        ) {
            CustomButton(
                text = stringResource(R.string.aceptar),
                modifier = Modifier.weight(1f),
                enabled = isAtBottom,
            ) {
                onAccept()
            }
            CustomButton(
                text = stringResource(R.string.rechazar),
                modifier = Modifier.weight(1f),
                enabled = true,
                style = UanButtonStyle.Danger
            ) {
                onDismiss()
            }
        }
    }
}