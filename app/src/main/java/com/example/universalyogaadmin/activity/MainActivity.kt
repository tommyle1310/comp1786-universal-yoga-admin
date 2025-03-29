package com.example.universalyogaadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this) // Initialize the database helper
        setContent {
            MainScreen(dbHelper)
        }
    }
}

@Composable
fun MainScreen(dbHelper: DatabaseHelper) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Add Class") },
                    label = { Text("Add Class") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "View Classes") },
                    label = { Text("View Classes") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Search, contentDescription = "Search Classes") },
                    label = { Text("Search Classes") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> AddClassScreen(Modifier.padding(innerPadding), dbHelper)
            1 -> ClassListScreen(Modifier.padding(innerPadding), dbHelper)
            2 -> SearchScreen(Modifier.padding(innerPadding), dbHelper)
        }
    }
}

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

        // Day of Week Dropdown
        ExposedDropdownMenuBox(
            expanded = isDayExpanded,
            onExpandedChange = { isDayExpanded = !isDayExpanded }
        ) {
            OutlinedTextField(
                value = day,
                onValueChange = { day = it },
                label = { Text("Day of the Week") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDayExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = isDayExpanded,
                onDismissRequest = { isDayExpanded = false },
                modifier = Modifier.exposedDropdownSize()
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

        // Time Dropdown
        ExposedDropdownMenuBox(
            expanded = isTimeExpanded,
            onExpandedChange = { isTimeExpanded = !isTimeExpanded }
        ) {
            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTimeExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = isTimeExpanded,
                onDismissRequest = { isTimeExpanded = false },
                modifier = Modifier.exposedDropdownSize()
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

        // Type of Class Dropdown (Đồng bộ với Day và Time)
        ExposedDropdownMenuBox(
            expanded = isTypeExpanded,
            onExpandedChange = { isTypeExpanded = !isTypeExpanded }
        ) {
            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Type of Class") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(), // Thêm menuAnchor để đồng bộ
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypeExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = isTypeExpanded,
                onDismissRequest = { isTypeExpanded = false },
                modifier = Modifier.exposedDropdownSize() // Thêm exposedDropdownSize để đồng bộ
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

        // Capacity (Number Keyboard)
        OutlinedTextField(
            value = capacity,
            onValueChange = { capacity = it },
            label = { Text("Capacity") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Bàn phím số
        )

        // Duration Dropdown (Thay vì TextField)
        ExposedDropdownMenuBox(
            expanded = isDurationExpanded,
            onExpandedChange = { isDurationExpanded = !isDurationExpanded }
        ) {
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (minutes)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDurationExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = isDurationExpanded,
                onDismissRequest = { isDurationExpanded = false },
                modifier = Modifier.exposedDropdownSize()
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

        // Price (Number Keyboard)
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Bàn phím số
        )

        // Description
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
                    dbHelper.addClass(yogaClass) // Save the class to the database
                } else {
                    // Show an error message if required fields are empty
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Class", color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassListScreen(modifier: Modifier = Modifier, dbHelper: DatabaseHelper) {
    val classes = remember { mutableStateListOf<YogaClass>() }

    LaunchedEffect(Unit) {
        classes.addAll(dbHelper.getAllClasses()) // Fetch classes from the database
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
                ClassItem(yogaClass)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassItem(yogaClass: YogaClass) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Day: ${yogaClass.day}", style = MaterialTheme.typography.bodyLarge)
            Text("Time: ${yogaClass.time}", style = MaterialTheme.typography.bodyMedium)
            Text("Capacity: ${yogaClass.capacity}", style = MaterialTheme.typography.bodyMedium)
            Text("Duration: ${yogaClass.duration} minutes", style = MaterialTheme.typography.bodyMedium)
            Text("Price: ${yogaClass.price}", style = MaterialTheme.typography.bodyMedium)
            Text("Type: ${yogaClass.type}", style = MaterialTheme.typography.bodyMedium)
            Text("Description: ${yogaClass.description ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier, dbHelper: DatabaseHelper) {
    var searchQuery by remember { mutableStateOf("") }
    val classes = remember { mutableStateListOf<YogaClass>() }

    LaunchedEffect(Unit) {
        classes.addAll(dbHelper.getAllClasses()) // Fetch classes from the database
    }

    val filteredClasses = classes.filter { it.type.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Search Classes", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search by Type") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn {
            items(filteredClasses) { yogaClass ->
                ClassItem(yogaClass)
            }
        }
    }
}