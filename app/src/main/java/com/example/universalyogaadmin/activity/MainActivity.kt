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
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.universalyogaadmin.ui.AddClassScreen
import com.example.universalyogaadmin.ui.ClassInstancesScreen
import com.example.universalyogaadmin.ui.ClassListScreen
import com.example.universalyogaadmin.ui.EditClassScreen
import com.example.universalyogaadmin.ui.EditInstanceScreen
import com.example.universalyogaadmin.ui.SearchScreen
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        setContent {
            val navController = rememberNavController()
            MainScreen(navController = navController, dbHelper = dbHelper)
        }
    }

    @Composable
    fun MainScreen(
        modifier: Modifier = Modifier,
        navController: NavHostController,
        dbHelper: DatabaseHelper
    ) {
        var selectedTab by remember { mutableStateOf(0) }

        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF111111), // Nền gần đen
                    contentColor = Color(0xFF58B427)          // Màu mặc định cho icon/chữ
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Add, contentDescription = "Add Class") },
                        label = { Text("Add Class") },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color(0xFF58B427),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0xFF58B427)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.List, contentDescription = "View Classes") },
                        label = { Text("View Classes") },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color(0xFF58B427),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0xFF58B427)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Search, contentDescription = "Search Classes") },
                        label = { Text("Search Classes") },
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color(0xFF58B427),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color(0xFF58B427)
                        )
                    )
                }
            }
        ) { innerPadding ->
            NavHost(navController = navController, startDestination = "add_class") {
                composable("add_class") {
                    AddClassScreen(modifier = Modifier.padding(innerPadding), dbHelper = dbHelper)
                }
                composable("class_list") {
                    ClassListScreen(
                        modifier = Modifier.padding(innerPadding),
                        dbHelper = dbHelper,
                        navController = navController
                    )
                }
                composable("search") {
                    SearchScreen(modifier = Modifier.padding(innerPadding), dbHelper = dbHelper)
                }
                composable("edit_class/{classId}") { backStackEntry ->
                    val classId = backStackEntry.arguments?.getString("classId")?.toIntOrNull() ?: 0
                    val yogaClass =
                        dbHelper.getAllClasses().find { it.id == classId } ?: return@composable
                    EditClassScreen(
                        modifier = Modifier.padding(innerPadding),
                        dbHelper = dbHelper,
                        yogaClass = yogaClass,
                        onSaveComplete = { navController.popBackStack() }
                    )
                }
                composable("class_instances/{classId}") { backStackEntry ->
                    val classId = backStackEntry.arguments?.getString("classId")?.toIntOrNull() ?: 0
                    ClassInstancesScreen(
                        modifier = Modifier.padding(innerPadding),
                        dbHelper = dbHelper,
                        classId = classId,
                        navController = navController
                    )
                }
                composable("edit_instance/{instanceId}") { backStackEntry ->
                    val instanceId =
                        backStackEntry.arguments?.getString("instanceId")?.toIntOrNull() ?: 0
                    val instance =
                        dbHelper.getAllInstances().find { it.id == instanceId } ?: return@composable
                    EditInstanceScreen(
                        modifier = Modifier.padding(innerPadding),
                        dbHelper = dbHelper,
                        instance = instance,
                        onSaveComplete = { navController.popBackStack() }
                    )
                }
            }

            LaunchedEffect(selectedTab) {
                when (selectedTab) {
                    0 -> navController.navigate("add_class") { popUpTo(navController.graph.startDestinationId) }
                    1 -> navController.navigate("class_list") { popUpTo(navController.graph.startDestinationId) }
                    2 -> navController.navigate("search") { popUpTo(navController.graph.startDestinationId) }
                }
            }
        }
    }
}