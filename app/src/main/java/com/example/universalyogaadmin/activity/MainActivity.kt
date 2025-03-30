package com.example.universalyogaadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.universalyogaadmin.ui.AddClassScreen
import com.example.universalyogaadmin.ui.ClassListScreen
import com.example.universalyogaadmin.ui.SearchScreen

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        setContent {
            MainScreen(dbHelper = dbHelper)
        }
    }

    @Composable
    fun MainScreen(modifier: Modifier = Modifier, dbHelper: DatabaseHelper) {
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
                0 -> AddClassScreen(modifier = Modifier.padding(innerPadding), dbHelper = dbHelper)
                1 -> ClassListScreen(modifier = Modifier.padding(innerPadding), dbHelper = dbHelper)
                2 -> SearchScreen(modifier = Modifier.padding(innerPadding), dbHelper = dbHelper)
            }
        }
    }
}