package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.unit.dp
import com.uan.epilepsyalarm20.ui.theme.reminderCardColors

@Composable
fun ReminderCard(
    title: String = "Reminder",
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(0.dp),
        colors = reminderCardColors(),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = style,
                color = MaterialTheme.colorScheme.primaryContainer,
                fontWeight = W400
            )
        }
    }
}