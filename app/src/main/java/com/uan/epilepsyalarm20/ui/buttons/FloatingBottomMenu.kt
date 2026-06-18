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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FloatingBottomMenu(
    modifier: Modifier = Modifier,
    onPauseClick: () -> Unit = {},
    onStopClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPauseClick) {
            Icon(
                imageVector = Icons.Default.Pause,
                contentDescription = "Pausar",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        IconButton(onClick = onStopClick) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "Detener",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}