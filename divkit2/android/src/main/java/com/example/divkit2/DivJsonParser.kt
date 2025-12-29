package com.example.divkit2

import kotlinx.serialization.json.Json

object DivJsonParser {

    private val jsonFormat = Json {
        ignoreUnknownKeys = true
        isLenient = true
        classDiscriminator = "type"
    }

    fun parse(json: String): DivComponent {
        return jsonFormat.decodeFromString(DivComponent.serializer(), json)
    }
}
