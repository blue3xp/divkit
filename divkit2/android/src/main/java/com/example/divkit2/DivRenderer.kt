package com.example.divkit2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment as ComposeAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun DivRenderer(component: DivComponent) {
    when (component) {
        is DivComponent.Text -> RenderText(component)
        is DivComponent.Image -> RenderImage(component)
        is DivComponent.Button -> RenderButton(component)
        is DivComponent.Container -> RenderContainer(component)
    }
}

@Composable
fun RenderText(data: DivComponent.Text) {
    Text(
        text = data.text,
        color = data.color.toColor(),
        fontSize = data.fontSize.sp,
        fontWeight = if (data.fontWeight == "bold") FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier.applyDivStyle(data.style)
    )
}

@Composable
fun RenderImage(data: DivComponent.Image) {
    Image(
        painter = ColorPainter(Color.LightGray),
        contentDescription = data.contentDescription,
        contentScale = when (data.contentScale) {
            "crop" -> ContentScale.Crop
            "fill" -> ContentScale.FillBounds
            else -> ContentScale.Fit
        },
        modifier = Modifier.applyDivStyle(data.style)
    )
}

@Composable
fun RenderButton(data: DivComponent.Button) {
    Button(
        onClick = { println("Button Action: ${data.onClickAction}") },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = data.backgroundColor.toColor(),
            contentColor = data.textColor.toColor()
        ),
        modifier = Modifier.applyDivStyle(data.style)
    ) {
        Text(text = data.text)
    }
}

@Composable
fun RenderContainer(data: DivComponent.Container) {
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
