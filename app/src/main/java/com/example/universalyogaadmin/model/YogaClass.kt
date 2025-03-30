package com.example.universalyogaadmin.model

data class YogaClass(
    val id: Int = 0,
    val day: String,
    val time: String,
    val capacity: String,
    val duration: String,
    val price: String,
    val type: String,
    val description: String?
)