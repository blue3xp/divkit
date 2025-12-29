package com.example.divkit2.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.divkit2.DivComponent
import com.example.divkit2.applyDivStyle
import com.example.divkit2.toColor

@Composable
fun DivTextRenderer(data: DivComponent.Text) {
    Text(
        text = data.text,
        color = data.color.toColor(),
        fontSize = data.fontSize.sp,
        fontWeight = if (data.fontWeight == "bold") FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier.applyDivStyle(data.style)
    )
}
