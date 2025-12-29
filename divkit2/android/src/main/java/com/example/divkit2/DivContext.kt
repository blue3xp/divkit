package com.example.divkit2

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

class DivContext {
    private val variables = mutableMapOf<String, MutableState<String>>()

    fun getVariable(name: String): MutableState<String> {
        return variables.getOrPut(name) { mutableStateOf("") }
    }

    fun getAllVariables(): Map<String, String> {
        return variables.mapValues { it.value.value }
    }
}

val LocalDivContext = compositionLocalOf<DivContext> { error("No DivContext provided") }
