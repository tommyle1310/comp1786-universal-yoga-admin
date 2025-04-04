package com.example.universalyogaadmin.ui

import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.*

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
    val yogaClass = dbHelper.getAllClasses().find { it.id == classId } // Lấy YogaClass tương ứng
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    LaunchedEffect(Unit) {
        instances.clear()
        instances.addAll(dbHelper.getInstancesForClass(classId))
    }

    // Hàm chuyển tên ngày trong tuần thành số thứ tự (1 = Sunday, 2 = Monday, ..., 7 = Saturday)
    fun getDayOfWeekNumber(dayName: String): Int {
        return when (dayName.lowercase()) {
            "sunday" -> Calendar.SUNDAY
            "monday" -> Calendar.MONDAY
            "tuesday" -> Calendar.TUESDAY
            "wednesday" -> Calendar.WEDNESDAY
            "thursday" -> Calendar.THURSDAY
            "friday" -> Calendar.FRIDAY
            "saturday" -> Calendar.SATURDAY
            else -> throw IllegalArgumentException("Invalid day name: $dayName")
        }
    }

    // Hàm mở DatePickerDialog
    val showDatePicker = {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val formattedDate = dateFormat.format(selectedDate.time)
            val selectedDayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK) // Lấy số thứ tự ngày trong tuần

            // Kiểm tra ngày trong tuần có khớp với YogaClass không
            if (yogaClass != null) {
                val classDayOfWeek = getDayOfWeekNumber(yogaClass.day)
                if (selectedDayOfWeek == classDayOfWeek) {
                    date = formattedDate // Cập nhật date ngay lập tức
                    errorMessage = ""
                } else {
                    errorMessage = "Selected date must match ${yogaClass.day} of the class"
                    date = "" // Reset date nếu không khớp
                }
            } else {
                errorMessage = "Class not found"
                date = ""
            }
        }, year, month, day).show()
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                TextButton(onClick = showDatePicker) {
                    Text("Pick")
                }
            }
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
                if (date.isBlank() || teacher.isBlank()) { // Dùng isBlank() để chắc chắn
                    errorMessage = "Date and Teacher are required"
                } else {
                    try {
                        dateFormat.parse(date) // Kiểm tra định dạng
                        dbHelper.addInstance(classId, date, teacher, comments)
                        instances.add(ClassInstance(0, classId, date, teacher, comments))
                        date = ""
                        teacher = ""
                        comments = ""
                        errorMessage = ""
                        Toast.makeText(context, "Instance added", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        errorMessage = "Invalid date format. Use dd/MM/yyyy"
                    }
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