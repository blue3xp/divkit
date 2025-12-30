package com.example.divkit2.components

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.divkit2.DivComponent
import com.example.divkit2.LocalDivContext
import com.example.divkit2.applyDivStyle
import com.example.divkit2.toColor

@Composable
fun DivButtonRenderer(data: DivComponent.Button) {
    val context = LocalDivContext.current

    Button(
        onClick = {
            val action = data.onClickAction
            // Delegate to the ActionHandler chain
            val handled = context.actionHandler.handleAction(action, context)

            if (!handled) {
                // Fallback / Default handling
                println("Unhandled Button Action: $action")
            }
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = data.backgroundColor.toColor(),
            contentColor = data.textColor.toColor()
        ),
        modifier = Modifier.applyDivStyle(data.style)
    ) {
        Text(text = data.text)
    }
}
