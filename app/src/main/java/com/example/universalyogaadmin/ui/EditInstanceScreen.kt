package com.example.universalyogaadmin.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.universalyogaadmin.DatabaseHelper
import com.example.universalyogaadmin.model.ClassInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditInstanceScreen(
    modifier: Modifier = Modifier,
    dbHelper: DatabaseHelper,
    instance: ClassInstance,
    onSaveComplete: () -> Unit
) {
    var date by remember { mutableStateOf(instance.date) }
    var teacher by remember { mutableStateOf(instance.teacher) }
    var comments by remember { mutableStateOf(instance.comments ?: "") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Edit Instance", style = MaterialTheme.typography.headlineMedium)

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
                    val updatedInstance = ClassInstance(instance.id, instance.classId, date, teacher, comments)
                    dbHelper.updateInstance(updatedInstance)
                    Toast.makeText(context, "Instance updated", Toast.LENGTH_SHORT).show()
                    onSaveComplete()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}