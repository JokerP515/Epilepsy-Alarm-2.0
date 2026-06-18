package com.uan.epilepsyalarm20.ui.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton (
    text: String,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun CustomButton2 (
    text: String,
    color: ButtonColors = ButtonDefaults.buttonColors(),
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp),
        shape = RoundedCornerShape(0.dp),
        colors = color,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}