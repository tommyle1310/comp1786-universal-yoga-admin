package com.example.universalyogaadmin.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.universalyogaadmin.DatabaseHelper
import com.example.universalyogaadmin.model.ClassInstance
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassInstancesScreen(modifier: Modifier = Modifier, dbHelper: DatabaseHelper, classId: Int) {
    val instances = remember { mutableStateListOf<ClassInstance>() }
    var date by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val yogaClass = dbHelper.getAllClasses().find { it.id == classId } // Lấy YogaClass tương ứng
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    LaunchedEffect(Unit) {
        instances.clear()
        instances.addAll(dbHelper.getInstancesForClass(classId))
    }

    // Hàm mở DatePickerDialog
    fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val formattedDate = dateFormat.format(selectedDate.time)
            val dayOfWeek = selectedDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())

            // Kiểm tra ngày trong tuần có khớp với YogaClass không
            if (yogaClass != null && dayOfWeek.equals(yogaClass.day, ignoreCase = true)) {
                date = formattedDate
                errorMessage = ""
            } else {
                errorMessage = "Selected date must match ${yogaClass?.day} of the class"
            }
        }, year, month, day).show()
    }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Instances for Class #$classId", style = MaterialTheme.typography.headlineMedium)

        // Date field với lịch popover
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date* (dd/MM/yyyy)") },
            readOnly = true, // Chỉ cho phép chọn từ lịch
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            trailingIcon = {
                TextButton(onClick = { showDatePicker() }) {
                    Text("Pick")
                }
            }
        )
        OutlinedTextField(value = teacher, onValueChange = { teacher = it }, label = { Text("Teacher*") })
        OutlinedTextField(value = comments, onValueChange = { comments = it }, label = { Text("Comments") })

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Button(onClick = {
            if (date.isEmpty() || teacher.isEmpty()) {
                errorMessage = "Date and Teacher are required"
            } else {
                try {
                    dateFormat.parse(date) // Kiểm tra định dạng
                    dbHelper.addInstance(classId, date, teacher, comments)
                    instances.add(ClassInstance(0, classId, date, teacher, comments))
                    date = ""; teacher = ""; comments = ""
                    errorMessage = ""
                } catch (e: Exception) {
                    errorMessage = "Invalid date format. Use dd/MM/yyyy"
                }
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