package com.example.universalyogaadmin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Green40,          // Xanh lá đậm từ Color.kt
    onPrimary = Color.White,
    secondary = Color(0xFF111111), // Almost black cho NavigationBar
    onSecondary = Color.White,     // Trắng tinh cho chữ/icon trên secondary
    background = GreenGrey80,   // Nền xám xanh nhạt
    onBackground = Color.Black,
    surface = Yellow80,         // Vàng nhạt cho surface
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}