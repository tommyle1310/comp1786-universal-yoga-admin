package com.example.universalyogaadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.universalyogaadmin.model.YogaClass
import com.example.universalyogaadmin.ui.components.ClassItem

class ClassListActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        setContent {
            ClassListScreen(
                modifier = Modifier,
                dbHelper = dbHelper
            ) // Truyền cả modifier và dbHelper
        }
    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ClassListScreen(modifier: Modifier = Modifier, dbHelper: DatabaseHelper) {
    val classes = remember { mutableStateListOf<YogaClass>() }

    LaunchedEffect(Unit) {
        classes.addAll(dbHelper.getAllClasses()) // Fetch classes từ database
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
                ClassItem(yogaClass) // Gọi ClassItem đã định nghĩa
            }
        }
    }
}
