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
fun ClassListScreen(modifier: Modifier = Modifier, dbHelper: DatabaseHelper) {
    val classes = remember { mutableStateListOf<YogaClass>() }

    LaunchedEffect(Unit) {
        classes.addAll(dbHelper.getAllClasses())
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Class List", style = MaterialTheme.typography.headlineMedium)

        LazyColumn {
            items(classes) { yogaClass ->
                ClassItem(yogaClass = yogaClass)
            }
        }
    }
}