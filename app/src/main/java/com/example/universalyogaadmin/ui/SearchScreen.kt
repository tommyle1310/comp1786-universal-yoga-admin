package com.example.universalyogaadmin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.universalyogaadmin.DatabaseHelper
import com.example.universalyogaadmin.model.YogaClass
import com.example.universalyogaadmin.ui.components.ClassItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier, dbHelper: DatabaseHelper) {
    var searchQuery by remember { mutableStateOf("") }
    val classes = remember { mutableStateListOf<YogaClass>() }

    LaunchedEffect(Unit) {
        classes.addAll(dbHelper.getAllClasses())
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
                ClassItem(yogaClass = yogaClass)
            }
        }
    }
}