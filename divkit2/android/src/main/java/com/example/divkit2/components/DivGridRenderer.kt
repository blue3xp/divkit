package com.example.divkit2.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.divkit2.DivComponent
import com.example.divkit2.DivRenderer
import com.example.divkit2.applyDivStyle

@Composable
fun DivGridRenderer(data: DivComponent.Grid) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(data.columnCount),
        modifier = Modifier.applyDivStyle(data.style)
    ) {
        items(
            items = data.items,
            span = { item ->
                // Apply column span if specified, otherwise default to 1
                val span = item.style.columnSpan ?: 1
                GridItemSpan(span.coerceAtMost(data.columnCount))
            }
        ) { child ->
            DivRenderer(child)
        }
    }
}
