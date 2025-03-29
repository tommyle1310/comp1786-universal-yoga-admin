package com.example.universalyogaadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class SearchActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        setContent {
            SearchScreen()
        }
    }

    @Composable
    fun SearchScreen(modifier: Modifier = Modifier) {
        var searchQuery by remember { mutableStateOf("") }
        val classes = remember {
            mutableStateListOf(
                YogaClass(day = "Monday", time = "10:00", capacity = "20", duration = "60", price = "$10", type = "Flow Yoga", description = "Beginner-friendly"),
                YogaClass(day = "Tuesday", time = "11:00", capacity = "15", duration = "90", price = "$15", type = "Aerial Yoga", description = "Intermediate level")
            )
        }

        val filteredClasses = classes.filter { it.type.contains(searchQuery, ignoreCase = true) }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Search Classes", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by Type") },
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn {
                items(filteredClasses) { yogaClass ->
                    ClassItem(yogaClass)
                }
            }
        }
    }

    @Composable
    fun ClassItem(yogaClass: YogaClass) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Day: ${yogaClass.day}", style = MaterialTheme.typography.bodyLarge)
                Text("Time: ${yogaClass.time}", style = MaterialTheme.typography.bodyMedium)
                Text("Capacity: ${yogaClass.capacity}", style = MaterialTheme.typography.bodyMedium)
                Text("Duration: ${yogaClass.duration} minutes", style = MaterialTheme.typography.bodyMedium)
                Text("Price: ${yogaClass.price}", style = MaterialTheme.typography.bodyMedium)
                Text("Type: ${yogaClass.type}", style = MaterialTheme.typography.bodyMedium)
                Text("Description: ${yogaClass.description ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}