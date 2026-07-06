package com.uan.epilepsyalarm20.ui.buttons

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uan.designsystem.uikit.components.UanButton
import com.uan.designsystem.uikit.components.UanCardDefaults
import com.uan.designsystem.uikit.theme.UanThemeTokens

@Composable
fun CustomButton (
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {},
) {
    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    UanButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = UanCardDefaults.titleStyle,
            color = colors.onSurface,
        )
    }
}