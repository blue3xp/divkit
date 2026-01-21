package com.example.divkit2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.divkit2.DivComponent
import com.example.divkit2.applyDivStyle

@Composable
fun DivCustomRenderer(data: DivComponent.Custom) {
    // Mock API call to get custom view
    // In a real app, this would use a PluginRegistry or similar
    val view = mockGetCustomView(data.plugin, data.message)

    Box(modifier = Modifier.applyDivStyle(data.style)) {
        view()
    }
}

fun mockGetCustomView(plugin: String, message: String): @Composable () -> Unit {
    // TODO: Replace with real API call
    if (plugin == "homehub" && message == "getTutorialCard") {
        return {
            // Simulated Tutorial Card
            Box(
                modifier = Modifier
                    .background(Color(0xFFE3F2FD))
                    .padding(16.dp)
            ) {
                Text("Custom Tutorial Card (Plugin: $plugin)")
            }
        }
    }
    return {
        Text("Unknown Custom View: $plugin / $message")
    }
}
