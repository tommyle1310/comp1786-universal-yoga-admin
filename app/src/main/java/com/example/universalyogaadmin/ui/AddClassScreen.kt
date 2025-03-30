package com.example.universalyogaadmin.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.universalyogaadmin.DatabaseHelper
import com.example.universalyogaadmin.R
import com.example.universalyogaadmin.model.YogaClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClassScreen(modifier: Modifier = Modifier, dbHelper: DatabaseHelper) {
    var day by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var isDayExpanded by remember { mutableStateOf(false) }
    var isTimeExpanded by remember { mutableStateOf(false) }
    var isTypeExpanded by remember { mutableStateOf(false) }
    var isDurationExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val days = context.resources.getStringArray(R.array.days_of_week).toList()
    val times = context.resources.getStringArray(R.array.times).toList()
    val types = context.resources.getStringArray(R.array.class_types).toList()
    val durations = context.resources.getStringArray(R.array.durations).toList()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Add Yoga Class", style = MaterialTheme.typography.headlineMedium)

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

        Button(
            onClick = {
                if (day.isNotEmpty() && time.isNotEmpty() && capacity.isNotEmpty() && duration.isNotEmpty() && price.isNotEmpty() && type.isNotEmpty()) {
                    val yogaClass = YogaClass(
                        day = day,
                        time = time,
                        capacity = capacity,
                        duration = duration,
                        price = price,
                        type = type,
                        description = description
                    )
                    dbHelper.addClass(yogaClass)
                    Toast.makeText(context, "Class added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Class", color = Color.White)
        }
    }
}