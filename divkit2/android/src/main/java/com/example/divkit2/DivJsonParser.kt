package com.example.divkit2

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

object DivJsonParser {

    private val jsonFormat = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    data class Template(
        val message: String,
        val plugin: String
    )

    fun parse(json: String): DivComponent {
        val rootElement = jsonFormat.parseToJsonElement(json).jsonObject

        val templatesMap = mutableMapOf<String, Template>()
        val templatesNode = rootElement["templates"]?.jsonObject

        templatesNode?.forEach { (key, value) ->
            val obj = value.jsonObject
            val message = obj["message"]?.jsonPrimitive?.content ?: ""
            val plugin = obj["plugin"]?.jsonPrimitive?.content ?: ""
            templatesMap[key] = Template(message, plugin)
        }

        val cardElement = rootElement["card"] ?: return DivComponent.Text("No card found")
        return parseComponent(cardElement, templatesMap)
    }

    private fun parseComponent(element: JsonElement, templates: Map<String, Template>): DivComponent {
        if (element is JsonPrimitive && element.isString) {
            // It's a reference to a template
            val templateId = element.content
            val template = templates[templateId]
            return if (template != null) {
                DivComponent.Custom(
                    message = template.message,
                    plugin = template.plugin
                )
            } else {
                DivComponent.Text("Template not found: $templateId")
            }
        }

        val json = element.jsonObject
        val type = json["type"]?.jsonPrimitive?.content
        val style = parseStyle(json["style"]?.jsonObject)

        return when (type) {
            "text" -> {
                DivComponent.Text(
                    text = json["text"]?.jsonPrimitive?.content ?: "",
                    fontSize = json["font_size"]?.jsonPrimitive?.intOrNull ?: 16,
                    color = parseColor(json["text_color"]?.jsonPrimitive?.content) ?: 0xFF000000,
                    fontWeight = json["font_weight"]?.jsonPrimitive?.content ?: "regular",
                    style = style
                )
            }
            "image" -> {
                DivComponent.Image(
                    url = json["url"]?.jsonPrimitive?.content ?: "",
                    contentDescription = json["content_description"]?.jsonPrimitive?.content,
                    contentScale = json["scale"]?.jsonPrimitive?.content ?: "fit",
                    style = style
                )
            }
            "input" -> {
                val validatorsJson = json["validators"]?.jsonArray
                val validators = validatorsJson?.map {
                    val vObj = it.jsonObject
                    DivValidator(
                        regex = vObj["regex"]?.jsonPrimitive?.content ?: "",
                        message = vObj["message"]?.jsonPrimitive?.content ?: ""
                    )
                } ?: emptyList()

                DivComponent.Input(
                    hint = json["hint"]?.jsonPrimitive?.content ?: "",
                    variable = json["variable"]?.jsonPrimitive?.content ?: "",
                    validators = validators,
                    style = style
                )
            }
            "button" -> {
                val actionObj = json["action"]?.jsonObject
                val action = DivAction(
                    url = actionObj?.get("url")?.jsonPrimitive?.content,
                    logId = actionObj?.get("log_id")?.jsonPrimitive?.content
                )

                DivComponent.Button(
                    text = json["text"]?.jsonPrimitive?.content ?: "",
                    onClickAction = action,
                    backgroundColor = parseColor(json["background_color"]?.jsonPrimitive?.content) ?: 0xFF2196F3,
                    textColor = parseColor(json["text_color"]?.jsonPrimitive?.content) ?: 0xFFFFFFFF,
                    style = style
                )
            }
            "container" -> {
                val itemsArray = json["items"]?.jsonArray ?: JsonArray(emptyList())
                val items = itemsArray.map { parseComponent(it, templates) }

                val orientStr = json["orientation"]?.jsonPrimitive?.content
                val orientation = when(orientStr) {
                    "horizontal" -> Orientation.HORIZONTAL
                    "overlap" -> Orientation.OVERLAP
                    "scroll_vertical" -> Orientation.SCROLL_VERTICAL
                    "scroll_horizontal" -> Orientation.SCROLL_HORIZONTAL
                    "wrap" -> Orientation.WRAP
                    else -> Orientation.VERTICAL
                }

                DivComponent.Container(
                    items = items,
                    orientation = orientation,
                    contentAlignmentHorizontal = parseAlignment(json["alignment_horizontal"]?.jsonPrimitive?.content) ?: Alignment.START,
                    contentAlignmentVertical = parseAlignment(json["alignment_vertical"]?.jsonPrimitive?.content) ?: Alignment.TOP,
                    style = style
                )
            }
            "grid" -> {
                val itemsArray = json["items"]?.jsonArray ?: JsonArray(emptyList())
                val items = itemsArray.map { parseComponent(it, templates) }

                DivComponent.Grid(
                    items = items,
                    columnCount = json["column_count"]?.jsonPrimitive?.intOrNull ?: 2,
                    style = style
                )
            }
            else -> DivComponent.Text("Unknown type: $type", style = style)
        }
    }

    private fun parseStyle(json: JsonObject?): DivStyle {
        if (json == null) return DivStyle()

        return DivStyle(
            width = parseDpOrNull(json["width"]?.jsonPrimitive?.intOrNull),
            height = parseDpOrNull(json["height"]?.jsonPrimitive?.intOrNull),
            background = parseColor(json["background"]?.jsonPrimitive?.content),
            padding = parseDp(json["padding"]?.jsonPrimitive?.intOrNull),
            margin = parseDp(json["margin"]?.jsonPrimitive?.intOrNull),
            border = parseBorder(json["border"]?.jsonObject),
            columnSpan = json["column_span"]?.jsonPrimitive?.intOrNull,
            rowSpan = json["row_span"]?.jsonPrimitive?.intOrNull
        )
    }

    private fun parseBorder(json: JsonObject?): DivBorder? {
        if (json == null) return null
        return DivBorder(
            color = parseColor(json["color"]?.jsonPrimitive?.content) ?: 0xFF000000,
            width = parseDp(json["width"]?.jsonPrimitive?.intOrNull),
            cornerRadius = parseDp(json["radius"]?.jsonPrimitive?.intOrNull)
        )
    }

    private fun parseDp(value: Int?): Dp {
        if (value == null) return 0.dp
        return if (value < 0) Dp.Unspecified else value.dp
    }

    // Helper needed for properties that can be truly null (not just 0)
    private fun parseDpOrNull(value: Int?): Dp? {
        if (value == null) return null
        return if (value < 0) Dp.Unspecified else value.dp
    }

    private fun parseColor(colorStr: String?): Long? {
        if (colorStr == null) return null
        return try {
            if (colorStr.startsWith("#")) {
                val hex = colorStr.substring(1)
                if (hex.length == 6) {
                    ("FF$hex").toLong(16)
                } else {
                    hex.toLong(16)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun parseAlignment(str: String?): Alignment? {
        return when(str) {
            "start" -> Alignment.START
            "center" -> Alignment.CENTER
            "end" -> Alignment.END
            "top" -> Alignment.TOP
            "bottom" -> Alignment.BOTTOM
            else -> null
        }
    }
}
