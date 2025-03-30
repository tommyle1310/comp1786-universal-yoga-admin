package com.example.universalyogaadmin.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF58B427),
    secondary = Color(0xFF3C7E1B),
    background = Color(0xFFF5F5F5),
    onBackground = Color(0xFF212121),
    error = Color(0xFFB00020)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}