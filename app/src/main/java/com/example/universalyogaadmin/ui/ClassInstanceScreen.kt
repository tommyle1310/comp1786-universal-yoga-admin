package com.example.universalyogaadmin.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.universalyogaadmin.DatabaseHelper
import com.example.universalyogaadmin.model.ClassInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassInstancesScreen(
    modifier: Modifier = Modifier,
    dbHelper: DatabaseHelper,
    classId: Int,
    navController: NavHostController
) {
    val instances = remember { mutableStateListOf<ClassInstance>() }
    var date by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        instances.clear()
        instances.addAll(dbHelper.getInstancesForClass(classId))
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Instances for Class #$classId", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date* (e.g., 17/10/2023)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = teacher,
            onValueChange = { teacher = it },
            label = { Text("Teacher*") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = comments,
            onValueChange = { comments = it },
            label = { Text("Comments (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                if (date.isEmpty() || teacher.isEmpty()) {
                    errorMessage = "Date and Teacher are required"
                } else {
                    dbHelper.addInstance(classId, date, teacher, comments)
                    instances.add(ClassInstance(0, classId, date, teacher, comments))
                    date = ""
                    teacher = ""
                    comments = ""
                    errorMessage = ""
                    Toast.makeText(context, "Instance added", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Instance")
        }

        LazyColumn {
            items(instances) { instance ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Date: ${instance.date}")
                            Text("Teacher: ${instance.teacher}")
                            Text("Comments: ${instance.comments ?: "N/A"}")
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = {
                                navController.navigate("edit_instance/${instance.id}")
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = {
                                dbHelper.deleteInstance(instance.id)
                                instances.remove(instance)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}