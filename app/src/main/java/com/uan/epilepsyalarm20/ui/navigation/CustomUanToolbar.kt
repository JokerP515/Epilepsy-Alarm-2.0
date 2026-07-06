package com.uan.epilepsyalarm20.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.uan.designsystem.uikit.components.UanToolbarDefaults
import com.uan.designsystem.uikit.foundation.UanInteractiveDefaults
import com.uan.designsystem.uikit.foundation.rememberUanReduceMotion
import com.uan.designsystem.uikit.theme.UanThemeTokens

data class CustomToolbarItemData(
    val contentDescription: String,
    val icon: @Composable () -> Unit
)

@Composable
fun CustomUanToolbar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    items: List<CustomToolbarItemData>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = UanThemeTokens.current.colors.surface,
    selectedIndicatorColor: Color = UanThemeTokens.current.colors.primary,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(UanThemeTokens.current.grid.touchTarget + UanThemeTokens.current.spacing.xl)
            .background(containerColor)
            .alpha(if (enabled) 1f else UanInteractiveDefaults.disabledAlpha)
            .padding(
                bottom = WindowInsets.systemBars.asPaddingValues()
                    .calculateBottomPadding()
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            ToolbarItem(
                index = index,
                selected = selectedIndex == index,
                enabled = enabled,
                contentDescription = item.contentDescription,
                selectedIndicatorColor = selectedIndicatorColor,
                onClick = { onItemSelected(index) },
                content = item.icon,
            )
        }
    }
}

@Composable
private fun ToolbarItem(
    index: Int,
    selected: Boolean,
    enabled: Boolean,
    contentDescription: String,
    selectedIndicatorColor: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val tokens = UanThemeTokens.current
    val reduceMotion = rememberUanReduceMotion()
    val motionDuration = if (reduceMotion) 0 else tokens.motion.durationShort
    val interactionSource = remember { MutableInteractionSource() }

    val indicatorColor by animateColorAsState(
        targetValue = if (selected) selectedIndicatorColor else Color.Transparent,
        animationSpec = tween(motionDuration),
        label = "toolbarIndicator$index",
    )

    val stateDesc = when {
        !enabled -> "inhabilitado"
        selected -> "seleccionado"
        else -> "no seleccionado"
    }

    Box(
        modifier = Modifier
            .size(UanInteractiveDefaults.minTouchTarget)
            .clip(CircleShape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                role = Role.Tab,
                onClick = onClick,
            )
            .semantics {
                this.contentDescription = contentDescription
                role = Role.Tab
                this.selected = selected
                this.stateDescription = stateDesc
                if (!enabled) disabled()
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(UanToolbarDefaults.indicatorSize)
                .clip(CircleShape)
                .background(indicatorColor, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}