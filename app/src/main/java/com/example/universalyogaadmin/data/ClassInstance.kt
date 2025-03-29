package com.example.universalyogaadmin

data class ClassInstance(
    val id: Int = 0,
    val classId: Int,
    val date: String,
    val teacher: String,
    val comments: String? = null
)