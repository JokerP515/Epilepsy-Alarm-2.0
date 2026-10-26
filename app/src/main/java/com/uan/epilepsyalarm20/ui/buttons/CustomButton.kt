package com.uan.epilepsyalarm20.ui.buttons

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uan.designsystem.uikit.components.UanButton
import com.uan.designsystem.uikit.components.UanButtonStyle
import com.uan.designsystem.uikit.theme.UanThemeTokens

@Composable
fun CustomButton (
    modifier: Modifier = Modifier,
    text: String,
    style: UanButtonStyle = UanButtonStyle.Primary,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val typography = tokens.typography
    UanButton(
        onClick = onClick,
        style = style,
        modifier = modifier,
        enabled = enabled
    ) {
        Text(
            text = text,
            style = typography.component,
            color = if (enabled) colors.onSurface else colors.onSurface.copy(alpha = 0.5f)
        )
    }
}