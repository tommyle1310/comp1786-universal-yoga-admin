package com.example.universalyogaadmin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.universalyogaadmin.DatabaseHelper
import com.example.universalyogaadmin.model.ClassInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassInstancesScreen(modifier: Modifier = Modifier, dbHelper: DatabaseHelper, classId: Int) {
    val instances = remember { mutableStateListOf<ClassInstance>() }
    var date by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        instances.clear()
        instances.addAll(dbHelper.getInstancesForClass(classId))
    }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Instances for Class #$classId", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date*") })
        OutlinedTextField(value = teacher, onValueChange = { teacher = it }, label = { Text("Teacher*") })
        OutlinedTextField(value = comments, onValueChange = { comments = it }, label = { Text("Comments") })

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Button(onClick = {
            if (date.isEmpty() || teacher.isEmpty()) {
                errorMessage = "Date and Teacher are required"
            } else {
                dbHelper.addInstance(classId, date, teacher, comments)
                instances.add(ClassInstance(0, classId, date, teacher, comments))
                date = ""; teacher = ""; comments = ""
                errorMessage = ""
            }
        }) {
            Text("Add Instance")
        }

        LazyColumn {
            items(instances) { instance ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Date: ${instance.date}")
                        Text("Teacher: ${instance.teacher}")
                        Text("Comments: ${instance.comments ?: "N/A"}")
                    }
                }
            }
        }
    }
}