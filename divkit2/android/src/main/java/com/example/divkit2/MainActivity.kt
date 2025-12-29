package com.example.divkit2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // Sample Data
                    val sampleData = DivComponent.Container(
                        orientation = Orientation.VERTICAL,
                        style = DivStyle(padding = 16.dp),
                        items = listOf(
                            DivComponent.Text(
                                text = "Welcome to DivKit 2.0 (Compose)",
                                fontSize = 24,
                                fontWeight = "bold",
                                style = DivStyle(margin = 8.dp)
                            ),
                            DivComponent.Image(
                                url = "https://example.com/logo.png",
                                style = DivStyle(width = 100.dp, height = 100.dp, background = 0xFFEEEEEE)
                            ),
                            DivComponent.Container(
                                orientation = Orientation.HORIZONTAL,
                                style = DivStyle(margin = 16.dp),
                                items = listOf(
                                    DivComponent.Button(
                                        text = "Cancel",
                                        onClickAction = DivAction(logId = "cancel"),
                                        backgroundColor = 0xFFE0E0E0,
                                        textColor = 0xFF000000,
                                        style = DivStyle(margin = 4.dp)
                                    ),
                                    DivComponent.Button(
                                        text = "Confirm",
                                        onClickAction = DivAction(logId = "confirm"),
                                        style = DivStyle(margin = 4.dp)
                                    )
                                )
                            )
                        )
                    )

                    DivRenderer(component = sampleData)
                }
            }
        }
    }
}
