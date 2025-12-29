package com.example.divkit2

import android.util.JsonReader
import android.util.JsonToken
import androidx.compose.ui.unit.dp
import java.io.StringReader

object DivJsonParser {

    fun parse(json: String): DivComponent {
        val reader = JsonReader(StringReader(json))
        reader.use {
            return readComponent(it)
        }
    }

    private fun readComponent(reader: JsonReader): DivComponent {
        // A component can be a direct object { "type": "...", ... }
        // or just the object if we assume the caller knows it's a component.
        // Here we assume the reader is at the start of an object.

        var type: String? = null
        // We might need to buffer attributes if "type" is not the first key,
        // but for simplicity/performance in this PoC, we expect standard JSON structure
        // or we use a temporary holder.
        // To be robust and streaming, we can't easily rewind.
        // So we will read all properties into a map or vars and then build.
        // For MAXIMUM performance with JsonReader on unknown order, we typically
        // have to accept constraints or use a slightly more complex state machine.
        // Here, let's just iterate and fill vars.

        // Common
        var style = DivStyle()

        // Text
        var text: String? = null
        var fontSize = 16
        var color: Long = 0xFF000000
        var fontWeight = "regular"

        // Image
        var imageUrl: String? = null
        var contentScale = "fit"

        // Button
        var btnText: String? = null
        var btnAction: DivAction? = null
        var btnBgColor: Long = 0xFF2196F3
        var btnTxtColor: Long = 0xFFFFFFFF

        // Container
        var items = mutableListOf<DivComponent>()
        var orientation = Orientation.VERTICAL
        var alignH = Alignment.START
        var alignV = Alignment.TOP

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "type" -> type = reader.nextString()
                "style" -> style = readStyle(reader)

                // Text specific
                "text" -> text = reader.nextString()
                "font_size" -> fontSize = reader.nextInt()
                "text_color" -> color = readColor(reader)
                "font_weight" -> fontWeight = reader.nextString()

                // Image specific
                "url" -> imageUrl = reader.nextString()
                "scale" -> contentScale = reader.nextString()

                // Button specific
                "action" -> btnAction = readAction(reader)
                "background_color" -> btnBgColor = readColor(reader)

                // Container specific
                "items" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        items.add(readComponent(reader))
                    }
                    reader.endArray()
                }
                "orientation" -> orientation = when(reader.nextString()) {
                    "horizontal" -> Orientation.HORIZONTAL
                    "overlap" -> Orientation.OVERLAP
                    else -> Orientation.VERTICAL
                }
                "alignment_horizontal" -> alignH = readAlignment(reader)
                "alignment_vertical" -> alignV = readAlignment(reader)

                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return when (type) {
            "text" -> DivComponent.Text(
                text = text ?: "",
                fontSize = fontSize,
                color = color,
                fontWeight = fontWeight,
                style = style
            )
            "image" -> DivComponent.Image(
                url = imageUrl ?: "",
                contentScale = contentScale,
                style = style
            )
            "button" -> DivComponent.Button(
                text = text ?: btnText ?: "Button",
                onClickAction = btnAction ?: DivAction(),
                backgroundColor = btnBgColor,
                textColor = btnTxtColor, // Reusing text_color if set, or default
                style = style
            )
            "container" -> DivComponent.Container(
                items = items,
                orientation = orientation,
                contentAlignmentHorizontal = alignH,
                contentAlignmentVertical = alignV,
                style = style
            )
            else -> DivComponent.Text("Unknown component: $type")
        }
    }

    private fun readStyle(reader: JsonReader): DivStyle {
        var width = androidx.compose.ui.unit.Dp.Unspecified
        var height = androidx.compose.ui.unit.Dp.Unspecified
        var background: Long? = null
        var padding = 0.dp
        var margin = 0.dp
        var border: DivBorder? = null
        var action: DivAction? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "width" -> width = reader.nextInt().dp
                "height" -> height = reader.nextInt().dp
                "background" -> background = readColor(reader)
                "padding" -> padding = reader.nextInt().dp
                "margin" -> margin = reader.nextInt().dp
                "border" -> border = readBorder(reader)
                "action" -> action = readAction(reader)
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return DivStyle(width, height, background, padding, margin, border, action)
    }

    private fun readBorder(reader: JsonReader): DivBorder {
        var color: Long = 0xFF000000
        var width = 0.dp
        var radius = 0.dp

        reader.beginObject()
        while(reader.hasNext()) {
            when(reader.nextName()) {
                "color" -> color = readColor(reader)
                "width" -> width = reader.nextInt().dp
                "radius" -> radius = reader.nextInt().dp
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return DivBorder(color, width, radius)
    }

    private fun readAction(reader: JsonReader): DivAction {
        var url: String? = null
        var logId: String? = null
        reader.beginObject()
        while(reader.hasNext()) {
            when(reader.nextName()) {
                "url" -> url = reader.nextString()
                "log_id" -> logId = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return DivAction(url, logId)
    }

    private fun readAlignment(reader: JsonReader): Alignment {
        return when(reader.nextString()) {
            "center" -> Alignment.CENTER
            "end" -> Alignment.END
            "bottom" -> Alignment.BOTTOM
            "start" -> Alignment.START
            "top" -> Alignment.TOP
            else -> Alignment.START
        }
    }

    private fun readColor(reader: JsonReader): Long {
        return if (reader.peek() == JsonToken.NUMBER) {
            reader.nextLong()
        } else {
            val colorStr = reader.nextString()
            try {
                if (colorStr.startsWith("#")) {
                    val hex = colorStr.substring(1)
                    if (hex.length == 6) {
                         ("FF$hex").toLong(16)
                    } else {
                        hex.toLong(16)
                    }
                } else {
                    0xFF000000
                }
            } catch (e: Exception) {
                0xFF000000
            }
        }
    }
}
