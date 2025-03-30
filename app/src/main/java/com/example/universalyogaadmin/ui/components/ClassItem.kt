package com.example.universalyogaadmin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.universalyogaadmin.model.YogaClass

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