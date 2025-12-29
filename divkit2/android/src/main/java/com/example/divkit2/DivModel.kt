package com.example.divkit2

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// --- Base Models ---

enum class Orientation {
    VERTICAL, HORIZONTAL, OVERLAP // OVERLAP maps to Box/ZStack
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

data class DivStyle(
    val width: Dp = Dp.Unspecified, // Wraps content by default
    val height: Dp = Dp.Unspecified,
    val background: Long? = null, // Color ARGB Long
    val padding: Dp = 0.dp,
    val margin: Dp = 0.dp,
    val border: DivBorder? = null,
    val action: DivAction? = null,
    val weight: Float? = null // For LinearLayout weight behavior
)

// --- Component Hierarchy ---

sealed class DivComponent {
    abstract val style: DivStyle

    data class Text(
        val text: String,
        val fontSize: Int = 16,
        val color: Long = 0xFF000000,
        val fontWeight: String = "regular", // "bold", "regular"
        override val style: DivStyle = DivStyle()
    ) : DivComponent()

    data class Image(
        val url: String,
        val contentDescription: String? = null,
        val contentScale: String = "fit", // "crop", "fit", "fill"
        override val style: DivStyle = DivStyle()
    ) : DivComponent()

    // Explicit Button component as requested
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
}
