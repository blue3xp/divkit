package com.example.divkit2.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.divkit2.DivComponent
import com.example.divkit2.LocalDivContext
import com.example.divkit2.applyDivStyle

@Composable
fun DivInputRenderer(data: DivComponent.Input) {
    val context = LocalDivContext.current
    val variable = context.getVariable(data.variable)

    // Validation State
    val isError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    fun validate(input: String) {
        var hasError = false
        var msg = ""
        for (validator in data.validators) {
            try {
                if (!Regex(validator.regex).matches(input)) {
                    hasError = true
                    msg = validator.message
                    break
                }
            } catch (e: Exception) {
                // Ignore invalid regex for stability
            }
        }
        isError.value = hasError
        errorMessage.value = msg
    }

    Column(modifier = Modifier.applyDivStyle(data.style)) {
        OutlinedTextField(
            value = variable.value,
            onValueChange = {
                variable.value = it
                validate(it)
            },
            label = { Text(data.hint) },
            isError = isError.value,
            modifier = Modifier // Style is applied to the Column container to handle spacing for error text
        )

        if (isError.value) {
            Text(
                text = errorMessage.value,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}
