package com.uan.epilepsyalarm20.ui.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uan.designsystem.uikit.components.UanCardDefaults
import com.uan.designsystem.uikit.foundation.UanIconButton
import com.uan.designsystem.uikit.theme.UanThemeTokens

@Composable
@Preview
fun FloatingBottomMenu(
    modifier: Modifier = Modifier,
    onPauseClick: () -> Unit = {},
    onStopClick: () -> Unit = {}
) {
    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val spacing = tokens.spacing
    
    Row(
        modifier = modifier
            .background(
                color = colors.primary,
                shape = RoundedCornerShape(UanCardDefaults.cornerRadius)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        UanIconButton(
            onClick = onPauseClick,
            contentDescription = "Pausar Alarma"
        ) {
            Icon(
                imageVector = Icons.Default.Pause,
                contentDescription = "Pausar",
                tint = colors.onSurface
            )
        }

        UanIconButton(
            onClick = onStopClick,
            contentDescription = "Detener Alarma"
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "Detener",
                tint = colors.onSurface
            )
        }
    }
}