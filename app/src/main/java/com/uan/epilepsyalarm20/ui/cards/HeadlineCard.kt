package com.uan.epilepsyalarm20.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp
import com.uan.designsystem.uikit.components.UanCardDefaults
import com.uan.designsystem.uikit.foundation.UanTone
import com.uan.designsystem.uikit.theme.UanThemeTokens

@Composable
fun HeadlineCard(title: String, description: String? = null) {

    val tokens = UanThemeTokens.current
    val colors = tokens.colors
    val borderColor = UanCardDefaults.borderColor(tone = UanTone.Neutral, enabled = true)
    val shape = RoundedCornerShape(UanCardDefaults.cornerRadius)

    OutlinedCard (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(UanCardDefaults.cornerRadius),
        border = androidx.compose.foundation.BorderStroke(
            width = UanCardDefaults.borderWidth,
            color = borderColor,
        )
    ) {
        Row(
            Modifier.fillMaxWidth()
                .padding(16.dp)
                .clip(shape),
            horizontalArrangement = Arrangement.spacedBy(UanCardDefaults.sectionSpacing)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",
                tint = colors.onSurface
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    style = UanCardDefaults.titleStyle,
                    color = colors.primary,
                    fontWeight = W600
                )
                if(description != null) {
                    Text(
                        text = description,
                        style = UanCardDefaults.bodyStyle,
                        color = colors.onSurface,
                        fontWeight = W400
                    )
                }
            }
        }
    }
}