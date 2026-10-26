package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uan.designsystem.uikit.components.UanCard
import com.uan.designsystem.uikit.foundation.UanTone

@Composable
fun ReminderCard(
    title: String = "Reminder"
) {
    UanCard(
        title = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        tone = UanTone.Warning
    )
}