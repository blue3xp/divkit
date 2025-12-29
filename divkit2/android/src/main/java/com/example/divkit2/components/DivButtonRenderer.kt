package com.example.divkit2.components

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.divkit2.DivComponent
import com.example.divkit2.applyDivStyle
import com.example.divkit2.toColor

@Composable
fun DivButtonRenderer(data: DivComponent.Button) {
    Button(
        onClick = { println("Button Action: ${data.onClickAction}") },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = data.backgroundColor.toColor(),
            contentColor = data.textColor.toColor()
        ),
        modifier = Modifier.applyDivStyle(data.style)
    ) {
        Text(text = data.text)
    }
}
