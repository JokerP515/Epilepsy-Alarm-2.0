package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.ui.theme.defaultCardColors

@Composable
fun HeadlineCard(title: String, description: String? = null) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = defaultCardColors()
    ) {
        Row(
            Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                if(description != null) {
                    Text(text = description, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}