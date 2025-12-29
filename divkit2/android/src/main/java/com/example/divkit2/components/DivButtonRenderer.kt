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
            println("Button Action: $action")

            if (action.logId == "submit_form") {
                val formData = context.getAllVariables()
                println("Submitting Form Data to ${action.url}: $formData")
                // In a real app, you would make a network request here
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
