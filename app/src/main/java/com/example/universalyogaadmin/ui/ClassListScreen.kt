package com.example.universalyogaadmin.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.universalyogaadmin.CloudSync
import com.example.universalyogaadmin.DatabaseHelper
import com.example.universalyogaadmin.model.YogaClass
import com.example.universalyogaadmin.ui.components.ClassItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassListScreen(
    modifier: Modifier = Modifier,
    dbHelper: DatabaseHelper,
    navController: NavHostController
) {
    val classes = remember { mutableStateListOf<YogaClass>() }
    var showResetDialog by remember { mutableStateOf(false) }
    var isSyncing by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        classes.addAll(dbHelper.getAllClasses())
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Database") },
            text = { Text("Are you sure you want to reset the database? All data will be lost.") },
            confirmButton = {
                Button(
                    onClick = {
                        dbHelper.writableDatabase.execSQL("DROP TABLE IF EXISTS ${DatabaseHelper.TABLE_CLASSES}")
                        dbHelper.writableDatabase.execSQL("DROP TABLE IF EXISTS ${DatabaseHelper.TABLE_INSTANCES}")
                        dbHelper.onCreate(dbHelper.writableDatabase)
                        classes.clear()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Yes") }
            },
            dismissButton = { Button(onClick = { showResetDialog = false }) { Text("No") } }
        )
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Class List", style = MaterialTheme.typography.headlineMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { showResetDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .height(40.dp)
                        .widthIn(min = 120.dp)
                ) { Text("Reset Database") }
                Button(
                    onClick = {
                        isSyncing = true
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val success = CloudSync.sync(context, dbHelper)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, if (success) "Sync successful" else "Sync failed", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Sync failed: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            } finally {
                                withContext(Dispatchers.Main) {
                                    isSyncing = false // Đảm bảo isSyncing luôn reset
                                }
                            }
                        }
                    },
                    enabled = !isSyncing,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF58B427)) ,
                    modifier = Modifier
                        .height(40.dp)
                        .widthIn(min = 120.dp)
                ) { Text(if (isSyncing) "Syncing..." else "Sync to Cloud") }
            }
        }

        LazyColumn {
            items(classes) { yogaClass ->
                ClassItem(
                    yogaClass = yogaClass,
                    onDelete = { dbHelper.deleteClass(yogaClass.id); classes.remove(yogaClass) },
                    onEdit = { navController.navigate("edit_class/${yogaClass.id}") },
                    onViewInstances = { navController.navigate("class_instances/${yogaClass.id}") }
                )
            }
        }
    }
}