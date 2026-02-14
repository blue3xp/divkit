package com.example.divkit2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Helper to convert ARGB Long to Color
fun Long.toColor(): Color = Color(this)

@Composable
fun Modifier.applyDivStyle(style: DivStyle): Modifier {
    var modifier = this

    // 1. Size & Weight
    // Note: In a real Column/Row, weight needs to be applied in a specific scope.
    // Here we handle fixed sizes.
    if (style.width != Dp.Unspecified) {
        modifier = modifier.width(style.width)
    }
    if (style.height != Dp.Unspecified) {
        modifier = modifier.height(style.height)
    }

    // 2. Margin (Outer padding)
    if (style.margin > 0.dp) {
        modifier = modifier.padding(style.margin)
    }

    // 3. Shape & Border & Background
    // We need a shape for clipping background and border
    val shape = if (style.border != null && style.border.cornerRadius > 0.dp) {
        RoundedCornerShape(style.border.cornerRadius)
    } else {
        RoundedCornerShape(0.dp)
    }

    // Clip content to shape
    modifier = modifier.clip(shape)

    // Background
    if (style.background != null) {
        modifier = modifier.background(style.background.toColor(), shape)
    }

    // Border
    if (style.border != null && style.border.width > 0.dp) {
        modifier = modifier.border(
            width = style.border.width,
            color = style.border.color.toColor(),
            shape = shape
        )
    }

    // 4. Action (Clickable)
    if (style.action != null) {
        modifier = modifier.clickable {
            // Handle action (e.g., log or open URL)
            println("DivAction Triggered: ${style.action}")
        }
    }

    // 5. Padding (Inner padding)
    if (style.padding > 0.dp) {
        modifier = modifier.padding(style.padding)
    }

    return modifier
}
