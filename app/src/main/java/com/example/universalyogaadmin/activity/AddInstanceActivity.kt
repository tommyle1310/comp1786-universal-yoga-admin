package com.example.universalyogaadmin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class AddInstanceActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        setContent {
            AddInstanceScreen()
        }
    }

    @Composable
    fun AddInstanceScreen() {
        var date by remember { mutableStateOf("") }
        var teacher by remember { mutableStateOf("") }
        var comments by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(value = date, onValueChange = { date = it }, label = { Text("Date") })
            TextField(value = teacher, onValueChange = { teacher = it }, label = { Text("Teacher") })
            TextField(value = comments, onValueChange = { comments = it }, label = { Text("Comments (Optional)") })

            Button(onClick = {
                if (date.isNotEmpty() && teacher.isNotEmpty()) {
                    // Add logic to save the instance
                    Toast.makeText(this@AddInstanceActivity, "Instance added successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity
                } else {
                    Toast.makeText(this@AddInstanceActivity, "Please fill all required fields!", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Add Instance")
            }
        }
    }
}