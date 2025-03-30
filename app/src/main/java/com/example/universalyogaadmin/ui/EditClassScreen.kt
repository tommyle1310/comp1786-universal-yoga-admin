package com.example.universalyogaadmin.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.universalyogaadmin.DatabaseHelper
import com.example.universalyogaadmin.model.YogaClass
import com.example.universalyogaadmin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClassScreen(
    modifier: Modifier = Modifier,
    dbHelper: DatabaseHelper,
    yogaClass: YogaClass, // Lớp học cần chỉnh sửa
    onSaveComplete: () -> Unit // Callback để quay lại sau khi lưu
) {
    // Khởi tạo state với dữ liệu từ yogaClass
    var day by remember { mutableStateOf(yogaClass.day) }
    var time by remember { mutableStateOf(yogaClass.time) }
    var capacity by remember { mutableStateOf(yogaClass.capacity) }
    var duration by remember { mutableStateOf(yogaClass.duration) }
    var price by remember { mutableStateOf(yogaClass.price) }
    var type by remember { mutableStateOf(yogaClass.type) }
    var description by remember { mutableStateOf(yogaClass.description ?: "") }

    var isDayExpanded by remember { mutableStateOf(false) }
    var isTimeExpanded by remember { mutableStateOf(false) }
    var isTypeExpanded by remember { mutableStateOf(false) }
    var isDurationExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val days = context.resources.getStringArray(R.array.days_of_week).toList()
    val times = context.resources.getStringArray(R.array.times).toList()
    val types = context.resources.getStringArray(R.array.class_types).toList()
    val durations = context.resources.getStringArray(R.array.durations).toList()

    var showConfirmation by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Confirm Edited Class Details") },
            text = {
                Text(
                    "Day: $day\nTime: $time\nCapacity: $capacity\nDuration: $duration\nPrice: $price\nType: $type\nDescription: $description"
                )
            },
            confirmButton = {
                Button(onClick = {
                    val updatedClass = YogaClass(
                        id = yogaClass.id, // Giữ nguyên ID
                        day = day,
                        time = time,
                        capacity = capacity,
                        duration = duration,
                        price = price,
                        type = type,
                        description = description
                    )
                    dbHelper.updateClass(updatedClass) // Cập nhật vào DB
                    Toast.makeText(context, "Class updated successfully", Toast.LENGTH_SHORT).show()
                    showConfirmation = false
                    onSaveComplete() // Quay lại sau khi lưu
                }) { Text("Save") }
            },
            dismissButton = {
                Button(onClick = { showConfirmation = false }) { Text("Edit More") }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Edit Yoga Class", style = MaterialTheme.typography.headlineMedium)

        ExposedDropdownMenuBox(
            expanded = isDayExpanded,
            onExpandedChange = { isDayExpanded = !isDayExpanded }
        ) {
            OutlinedTextField(
                value = day,
                onValueChange = { day = it },
                label = { Text("Day of the Week") },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDayExpanded) }
            )
            ExposedDropdownMenu(
                expanded = isDayExpanded,
                onDismissRequest = { isDayExpanded = false }
            ) {
                days.forEach { dayOption ->
                    DropdownMenuItem(
                        text = { Text(dayOption) },
                        onClick = {
                            day = dayOption
                            isDayExpanded = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = isTimeExpanded,
            onExpandedChange = { isTimeExpanded = !isTimeExpanded }
        ) {
            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time") },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTimeExpanded) }
            )
            ExposedDropdownMenu(
                expanded = isTimeExpanded,
                onDismissRequest = { isTimeExpanded = false }
            ) {
                times.forEach { timeOption ->
                    DropdownMenuItem(
                        text = { Text(timeOption) },
                        onClick = {
                            time = timeOption
                            isTimeExpanded = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = isTypeExpanded,
            onExpandedChange = { isTypeExpanded = !isTypeExpanded }
        ) {
            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Type of Class") },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypeExpanded) }
            )
            ExposedDropdownMenu(
                expanded = isTypeExpanded,
                onDismissRequest = { isTypeExpanded = false }
            ) {
                types.forEach { typeOption ->
                    DropdownMenuItem(
                        text = { Text(typeOption) },
                        onClick = {
                            type = typeOption
                            isTypeExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = capacity,
            onValueChange = { capacity = it },
            label = { Text("Capacity") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        ExposedDropdownMenuBox(
            expanded = isDurationExpanded,
            onExpandedChange = { isDurationExpanded = !isDurationExpanded }
        ) {
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (minutes)") },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDurationExpanded) }
            )
            ExposedDropdownMenu(
                expanded = isDurationExpanded,
                onDismissRequest = { isDurationExpanded = false }
            ) {
                durations.forEach { durationOption ->
                    DropdownMenuItem(
                        text = { Text("$durationOption minutes") },
                        onClick = {
                            duration = durationOption
                            isDurationExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                if (day.isNotEmpty() && time.isNotEmpty() && capacity.isNotEmpty() && duration.isNotEmpty() && price.isNotEmpty() && type.isNotEmpty()) {
                    errorMessage = ""
                    showConfirmation = true
                } else {
                    errorMessage = "Please fill all required fields"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}