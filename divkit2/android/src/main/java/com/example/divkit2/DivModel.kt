package com.example.divkit2

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator

// --- Serializers ---

object ColorSerializer : KSerializer<Long> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Long) {
        encoder.encodeString("#${String.format("%08X", value)}")
    }

    override fun deserialize(decoder: Decoder): Long {
        val string = decoder.decodeString()
        return try {
            if (string.startsWith("#")) {
                val hex = string.substring(1)
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

object DpSerializer : KSerializer<Dp> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Dp", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: Dp) {
        if (value == Dp.Unspecified) {
            encoder.encodeInt(-1)
        } else {
            encoder.encodeInt(value.value.toInt())
        }
    }

    override fun deserialize(decoder: Decoder): Dp {
        val intVal = decoder.decodeInt()
        return if (intVal < 0) Dp.Unspecified else intVal.dp
    }
}

// --- Base Models ---

@Serializable
enum class Orientation {
    @SerialName("vertical") VERTICAL,
    @SerialName("horizontal") HORIZONTAL,
    @SerialName("overlap") OVERLAP,
    @SerialName("scroll_vertical") SCROLL_VERTICAL,
    @SerialName("scroll_horizontal") SCROLL_HORIZONTAL
}

@Serializable
enum class Alignment {
    @SerialName("start") START,
    @SerialName("center") CENTER,
    @SerialName("end") END,
    @SerialName("top") TOP,
    @SerialName("bottom") BOTTOM
}

@Serializable
data class DivAction(
    val url: String? = null,
    @SerialName("log_id") val logId: String? = null
)

@Serializable
data class DivBorder(
    @Serializable(with = ColorSerializer::class) val color: Long = 0xFF000000,
    @Serializable(with = DpSerializer::class) val width: Dp = 0.dp,
    @SerialName("radius") @Serializable(with = DpSerializer::class) val cornerRadius: Dp = 0.dp
)

@Serializable
data class DivStyle(
    @Serializable(with = DpSerializer::class) val width: Dp = Dp.Unspecified,
    @Serializable(with = DpSerializer::class) val height: Dp = Dp.Unspecified,
    @Serializable(with = ColorSerializer::class) val background: Long? = null,
    @Serializable(with = DpSerializer::class) val padding: Dp = 0.dp,
    @Serializable(with = DpSerializer::class) val margin: Dp = 0.dp,
    val border: DivBorder? = null,
    val action: DivAction? = null,
    val weight: Float? = null
)

// --- Component Hierarchy ---

@Serializable
@JsonClassDiscriminator("type")
sealed class DivComponent {
    abstract val style: DivStyle

    @Serializable
    @SerialName("text")
    data class Text(
        val text: String,
        @SerialName("font_size") val fontSize: Int = 16,
        @SerialName("text_color") @Serializable(with = ColorSerializer::class) val color: Long = 0xFF000000,
        @SerialName("font_weight") val fontWeight: String = "regular",
        override val style: DivStyle = DivStyle()
    ) : DivComponent()

    @Serializable
    @SerialName("image")
    data class Image(
        val url: String,
        @SerialName("content_description") val contentDescription: String? = null,
        @SerialName("scale") val contentScale: String = "fit",
        override val style: DivStyle = DivStyle()
    ) : DivComponent()

    @Serializable
    @SerialName("button")
    data class Button(
        val text: String,
        @SerialName("action") val onClickAction: DivAction,
        @SerialName("background_color") @Serializable(with = ColorSerializer::class) val backgroundColor: Long = 0xFF2196F3,
        @SerialName("text_color") @Serializable(with = ColorSerializer::class) val textColor: Long = 0xFFFFFFFF,
        override val style: DivStyle = DivStyle()
    ) : DivComponent()

    @Serializable
    @SerialName("container")
    data class Container(
        val items: List<DivComponent>,
        val orientation: Orientation = Orientation.VERTICAL,
        @SerialName("alignment_horizontal") val contentAlignmentHorizontal: Alignment = Alignment.START,
        @SerialName("alignment_vertical") val contentAlignmentVertical: Alignment = Alignment.TOP,
        override val style: DivStyle = DivStyle()
    ) : DivComponent()
}
