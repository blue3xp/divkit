package com.example.divkit2.components

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.divkit2.DivComponent
import com.example.divkit2.LocalDivContext
import com.example.divkit2.applyDivStyle

@Composable
fun DivInputRenderer(data: DivComponent.Input) {
    val context = LocalDivContext.current
    val variable = context.getVariable(data.variable)

    OutlinedTextField(
        value = variable.value,
        onValueChange = { variable.value = it },
        label = { Text(data.hint) },
        modifier = Modifier.applyDivStyle(data.style)
    )
}
