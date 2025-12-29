package com.example.divkit2.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment as ComposeAlignment
import androidx.compose.ui.Modifier
import com.example.divkit2.Alignment
import com.example.divkit2.DivComponent
import com.example.divkit2.DivRenderer
import com.example.divkit2.Orientation
import com.example.divkit2.applyDivStyle

@Composable
fun DivContainerRenderer(data: DivComponent.Container) {
    val modifier = Modifier.applyDivStyle(data.style)

    when (data.orientation) {
        Orientation.VERTICAL -> {
            Column(
                modifier = modifier,
                horizontalAlignment = mapHorizontalAlignment(data.contentAlignmentHorizontal),
                verticalArrangement = mapVerticalArrangement(data.contentAlignmentVertical)
            ) {
                data.items.forEach { child ->
                    DivRenderer(child)
                }
            }
        }
        Orientation.HORIZONTAL -> {
            Row(
                modifier = modifier,
                verticalAlignment = mapVerticalAlignment(data.contentAlignmentVertical),
                horizontalArrangement = mapHorizontalArrangement(data.contentAlignmentHorizontal)
            ) {
                data.items.forEach { child ->
                    DivRenderer(child)
                }
            }
        }
        Orientation.OVERLAP -> {
            Box(
                modifier = modifier,
                contentAlignment = ComposeAlignment.TopStart
            ) {
                data.items.forEach { child ->
                    DivRenderer(child)
                }
            }
        }
        Orientation.SCROLL_VERTICAL -> {
            LazyColumn(
                modifier = modifier,
                horizontalAlignment = mapHorizontalAlignment(data.contentAlignmentHorizontal),
                verticalArrangement = mapVerticalArrangement(data.contentAlignmentVertical)
            ) {
                items(data.items) { child ->
                    DivRenderer(child)
                }
            }
        }
        Orientation.SCROLL_HORIZONTAL -> {
            LazyRow(
                modifier = modifier,
                verticalAlignment = mapVerticalAlignment(data.contentAlignmentVertical),
                horizontalArrangement = mapHorizontalArrangement(data.contentAlignmentHorizontal)
            ) {
                items(data.items) { child ->
                    DivRenderer(child)
                }
            }
        }
    }
}

// --- Mapper Helpers ---

fun mapHorizontalAlignment(align: Alignment): ComposeAlignment.Horizontal {
    return when (align) {
        Alignment.START -> ComposeAlignment.Start
        Alignment.CENTER -> ComposeAlignment.CenterHorizontally
        Alignment.END -> ComposeAlignment.End
        else -> ComposeAlignment.Start
    }
}

fun mapVerticalAlignment(align: Alignment): ComposeAlignment.Vertical {
    return when (align) {
        Alignment.TOP -> ComposeAlignment.Top
        Alignment.CENTER -> ComposeAlignment.CenterVertically
        Alignment.BOTTOM -> ComposeAlignment.Bottom
        else -> ComposeAlignment.Top
    }
}

fun mapVerticalArrangement(align: Alignment): Arrangement.Vertical {
    return when (align) {
        Alignment.TOP -> Arrangement.Top
        Alignment.CENTER -> Arrangement.Center
        Alignment.BOTTOM -> Arrangement.Bottom
        else -> Arrangement.Top
    }
}

fun mapHorizontalArrangement(align: Alignment): Arrangement.Horizontal {
    return when (align) {
        Alignment.START -> Arrangement.Start
        Alignment.CENTER -> Arrangement.Center
        Alignment.END -> Arrangement.End
        else -> Arrangement.Start
    }
}
