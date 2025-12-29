package com.example.divkit2.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import com.example.divkit2.DivComponent
import com.example.divkit2.applyDivStyle

@Composable
fun DivImageRenderer(data: DivComponent.Image) {
    // Placeholder logic from previous implementation
    Image(
        painter = ColorPainter(Color.LightGray),
        contentDescription = data.contentDescription,
        contentScale = when (data.contentScale) {
            "crop" -> ContentScale.Crop
            "fill" -> ContentScale.FillBounds
            else -> ContentScale.Fit
        },
        modifier = Modifier.applyDivStyle(data.style)
    )
}
