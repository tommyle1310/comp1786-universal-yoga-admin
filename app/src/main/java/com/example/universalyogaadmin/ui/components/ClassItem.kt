package com.example.universalyogaadmin.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.universalyogaadmin.model.YogaClass

@Composable
fun ClassItem(
    yogaClass: YogaClass,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onViewInstances: () -> Unit
) {
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
            Text(
                "Duration: ${yogaClass.duration} minutes",
                style = MaterialTheme.typography.bodyMedium
            )
            Text("Price: ${yogaClass.price}", style = MaterialTheme.typography.bodyMedium)
            Text("Type: ${yogaClass.type}", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Description: ${yogaClass.description ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.weight(1f)) // Đẩy nút "View Instances" sang phải
                Button(
                    onClick = onViewInstances,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF58B427))
                ) { Text("View Instances") }
            }
        }
    }
}