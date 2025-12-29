package com.example.divkit2

import androidx.compose.runtime.Composable
import com.example.divkit2.components.DivButtonRenderer
import com.example.divkit2.components.DivContainerRenderer
import com.example.divkit2.components.DivGridRenderer
import com.example.divkit2.components.DivImageRenderer
import com.example.divkit2.components.DivTextRenderer

@Composable
fun DivRenderer(component: DivComponent) {
    when (component) {
        is DivComponent.Text -> DivTextRenderer(component)
        is DivComponent.Image -> DivImageRenderer(component)
        is DivComponent.Button -> DivButtonRenderer(component)
        is DivComponent.Container -> DivContainerRenderer(component)
        is DivComponent.Grid -> DivGridRenderer(component)
    }
}
