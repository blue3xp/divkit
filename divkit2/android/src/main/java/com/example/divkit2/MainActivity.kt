package com.example.divkit2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val divData = remember { mutableStateOf<DivComponent?>(null) }
                    val divContext = remember { DivContext() }

                    // Simulate loading large/complex JSON
                    LaunchedEffect(Unit) {
                        withContext(Dispatchers.IO) {
                            // In a real app, this would come from a file or network
                            val complexJson = """
                                {
                                  "type": "container",
                                  "style": { "padding": 16 },
                                  "orientation": "vertical",
                                  "items": [
                                    {
                                      "type": "text",
                                      "text": "Parsed from JSON (Async)",
                                      "font_size": 24,
                                      "font_weight": "bold",
                                      "style": { "margin": 8 }
                                    },
                                    {
                                      "type": "input",
                                      "hint": "Enter your name",
                                      "variable": "user_name",
                                      "style": { "margin": 8, "width": -1 }
                                    },
                                    {
                                      "type": "container",
                                      "orientation": "horizontal",
                                      "style": { "margin": 16 },
                                      "items": [
                                        {
                                          "type": "button",
                                          "text": "Submit Form",
                                          "action": { "log_id": "submit_form", "url": "https://example.com/api/submit" },
                                          "background_color": "#FF2196F3",
                                          "text_color": "#FFFFFFFF",
                                          "style": { "margin": 4 }
                                        }
                                      ]
                                    }
                                  ]
                                }
                            """.trimIndent()

                            // Parse off-thread
                            val parsed = DivJsonParser.parse(complexJson)

                            // Simulate network delay to show loading state
                            kotlinx.coroutines.delay(1000)

                            divData.value = parsed
                        }
                    }

                    if (divData.value == null) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator()
                        }
                    } else {
                        CompositionLocalProvider(LocalDivContext provides divContext) {
                            DivRenderer(component = divData.value!!)
                        }
                    }
                }
            }
        }
    }
}
