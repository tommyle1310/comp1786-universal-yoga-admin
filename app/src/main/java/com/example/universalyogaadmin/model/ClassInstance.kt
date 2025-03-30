package com.example.universalyogaadmin.model

data class ClassInstance(
    val id: Int = 0,
    val classId: Int,
    val date: String,
    val teacher: String,
    val comments: String? = null
)