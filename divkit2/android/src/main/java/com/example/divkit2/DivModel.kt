package com.example.divkit2

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// --- Base Models ---

enum class Orientation {
    VERTICAL, HORIZONTAL, OVERLAP, SCROLL_VERTICAL, SCROLL_HORIZONTAL, WRAP
}

enum class Alignment {
    START, CENTER, END, TOP, BOTTOM
}

data class DivAction(
    val url: String? = null,
    val logId: String? = null
)

data class DivBorder(
    val color: Long = 0xFF000000,
    val width: Dp = 0.dp,
    val cornerRadius: Dp = 0.dp
)

data class DivValidator(
    val regex: String,
    val message: String
)

data class DivStyle(
    val width: Dp = Dp.Unspecified,
    val height: Dp = Dp.Unspecified,
    val background: Long? = null,
    val padding: Dp = 0.dp,
    val margin: Dp = 0.dp,
    val border: DivBorder? = null,
    val action: DivAction? = null,
    val weight: Float? = null,
    val columnSpan: Int? = null,
    val rowSpan: Int? = null
)

// --- Component Hierarchy ---

sealed class DivComponent {
    abstract val style: DivStyle

    data class Text(
        val text: String,
        val fontSize: Int = 16,
        val color: Long = 0xFF000000,
        val fontWeight: String = "regular",
        override val style: DivStyle = DivStyle()
    ) : DivComponent()

    data class Input(
        val hint: String = "",
        val variable: String,
        val validators: List<DivValidator> = emptyList(),
        override val style: DivStyle = DivStyle()
    ) : DivComponent()

    data class Image(
        val url: String,
        val contentDescription: String? = null,
        val contentScale: String = "fit",
        override val style: DivStyle = DivStyle()
    ) : DivComponent()

    data class Button(
        val text: String,
        val onClickAction: DivAction,
        val backgroundColor: Long = 0xFF2196F3,
        val textColor: Long = 0xFFFFFFFF,
        override val style: DivStyle = DivStyle()
    ) : DivComponent()

    data class Container(
        val items: List<DivComponent>,
        val orientation: Orientation = Orientation.VERTICAL,
        val contentAlignmentHorizontal: Alignment = Alignment.START,
        val contentAlignmentVertical: Alignment = Alignment.TOP,
        override val style: DivStyle = DivStyle()
    ) : DivComponent()

    data class Grid(
        val items: List<DivComponent>,
        val columnCount: Int = 2,
        override val style: DivStyle = DivStyle()
    ) : DivComponent()

    data class Custom(
        val message: String,
        val plugin: String,
        override val style: DivStyle = DivStyle()
    ) : DivComponent()
}
