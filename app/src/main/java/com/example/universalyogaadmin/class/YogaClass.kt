package com.example.universalyogaadmin

data class YogaClass(
    val id: Int = 0,
    val day: String,
    val time: String,
    val capacity: String, // Changed from Int to String
    val duration: String,
    val price: String, // Changed from Double to String
    val type: String,
    val description: String? = null
)