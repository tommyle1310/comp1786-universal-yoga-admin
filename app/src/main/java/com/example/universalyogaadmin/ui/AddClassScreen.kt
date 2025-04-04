package com.example.universalyogaadmin.ui

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
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

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var showConfirmation by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Confirm Class Details") },
            text = { Text("Day: $day\nTime: $time\nCapacity: $capacity\nDuration: $duration\nPrice: $price\nType: $type\nDescription: $description") },
            confirmButton = {
                Button(
                    onClick = {
                        dbHelper.addClass(
                            YogaClass(
                                day = day,
                                time = time,
                                capacity = capacity,
                                duration = duration,
                                price = price,
                                type = type,
                                description = description
                            )
                        )
                        showConfirmation = false
                        day = ""; time = ""; capacity = ""; duration = ""; price = ""; type =
                        ""; description = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF58B427))
                ) { Text("Save") }
            },
            dismissButton = { Button(onClick = { showConfirmation = false }) { Text("Edit") } }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Add Yoga Class", style = MaterialTheme.typography.headlineMedium)

        ExposedDropdownMenuBox(
            expanded = isDayExpanded,
            onExpandedChange = { isDayExpanded = !isDayExpanded }) {
            OutlinedTextField(
                value = day,
                onValueChange = { day = it },
                label = { Text("Day of the Week") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDayExpanded) }
            )
            ExposedDropdownMenu(
                expanded = isDayExpanded,
                onDismissRequest = { isDayExpanded = false }) {
                days.forEach { dayOption ->
                    DropdownMenuItem(
                        text = { Text(dayOption) },
                        onClick = { day = dayOption; isDayExpanded = false })
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = isTimeExpanded,
            onExpandedChange = { isTimeExpanded = !isTimeExpanded }) {
            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTimeExpanded) }
            )
            ExposedDropdownMenu(
                expanded = isTimeExpanded,
                onDismissRequest = { isTimeExpanded = false }) {
                times.forEach { timeOption ->
                    DropdownMenuItem(
                        text = { Text(timeOption) },
                        onClick = { time = timeOption; isTimeExpanded = false })
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = isTypeExpanded,
            onExpandedChange = { isTypeExpanded = !isTypeExpanded }) {
            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Type of Class") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypeExpanded) }
            )
            ExposedDropdownMenu(
                expanded = isTypeExpanded,
                onDismissRequest = { isTypeExpanded = false }) {
                types.forEach { typeOption ->
                    DropdownMenuItem(
                        text = { Text(typeOption) },
                        onClick = { type = typeOption; isTypeExpanded = false })
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
            onExpandedChange = { isDurationExpanded = !isDurationExpanded }) {
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (minutes)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDurationExpanded) }
            )
            ExposedDropdownMenu(
                expanded = isDurationExpanded,
                onDismissRequest = { isDurationExpanded = false }) {
                durations.forEach { durationOption ->
                    DropdownMenuItem(
                        text = { Text("$durationOption minutes") },
                        onClick = { duration = durationOption; isDurationExpanded = false }
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
                if (locationPermissionState.status.isGranted) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            description =
                                "Location: ${location.latitude}, ${location.longitude}\n$description"
                        }
                        Toast.makeText(context, "Location added to description", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    locationPermissionState.launchPermissionRequest()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Current Location")
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
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF58B427))
        ) {
            Text("Add Class")
        }

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}