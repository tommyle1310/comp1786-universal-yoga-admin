package com.example.universalyogaadmin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.universalyogaadmin.DatabaseHelper
import com.example.universalyogaadmin.model.ClassInstance
import com.example.universalyogaadmin.model.YogaClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier, dbHelper: DatabaseHelper) {
    var searchQuery by remember { mutableStateOf("") }
    var searchType by remember { mutableStateOf("type") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedClass by remember { mutableStateOf<YogaClass?>(null) } // Lưu class được chọn
    val classes = remember { mutableStateListOf<YogaClass>() }
    val instances = remember { mutableStateListOf<ClassInstance>() }

    LaunchedEffect(Unit) {
        classes.addAll(dbHelper.getAllClasses())
        instances.addAll(dbHelper.getAllInstances())
    }

    val filteredResults = when (searchType) {
        "type" -> classes.filter { it.type.contains(searchQuery, ignoreCase = true) }
        "teacher" -> instances.filter { it.teacher.contains(searchQuery, ignoreCase = true) }.map { classes.find { c -> c.id == it.classId }!! }
        "date" -> instances.filter { it.date.contains(searchQuery, ignoreCase = true) }.map { classes.find { c -> c.id == it.classId }!! }
        "day" -> classes.filter { it.day.contains(searchQuery, ignoreCase = true) }
        else -> emptyList()
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Search Classes", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                modifier = Modifier.weight(1f)
            )
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
                modifier = Modifier.width(120.dp)
            ) {
                TextField(
                    value = searchType.replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Type") },
                        onClick = { searchType = "type"; isDropdownExpanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Teacher") },
                        onClick = { searchType = "teacher"; isDropdownExpanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Date") },
                        onClick = { searchType = "date"; isDropdownExpanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Day") },
                        onClick = { searchType = "day"; isDropdownExpanded = false }
                    )
                }
            }
        }

        if (selectedClass != null) {
            AlertDialog(
                onDismissRequest = { selectedClass = null },
                title = { Text("Class Details") },
                text = {
                    Column {
                        Text("Type: ${selectedClass?.type}")
                        Text("Day: ${selectedClass?.day}")
                        Text("Time: ${selectedClass?.time}")
                        Text("Capacity: ${selectedClass?.capacity}")
                        Text("Duration: ${selectedClass?.duration}")
                        Text("Price: ${selectedClass?.price}")
                        Text("Description: ${selectedClass?.description ?: "N/A"}")
                        Text("Instances:")
                        val classInstances = instances.filter { it.classId == selectedClass?.id }
                        if (classInstances.isNotEmpty()) {
                            classInstances.forEach { instance ->
                                Text(" - Date: ${instance.date}, Teacher: ${instance.teacher}, Comments: ${instance.comments ?: "N/A"}")
                            }
                        } else {
                            Text("No instances available")
                        }
                    }
                },
                confirmButton = { Button(onClick = { selectedClass = null }) { Text("Close") } }
            )
        }

        LazyColumn {
            items(filteredResults) { yogaClass ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { selectedClass = yogaClass } // Chọn để xem chi tiết
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Type: ${yogaClass.type}")
                        Text("Day: ${yogaClass.day}")
                        Text("Time: ${yogaClass.time}")
                    }
                }
            }
        }
    }
}