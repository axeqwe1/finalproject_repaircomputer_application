package com.example.repaircomputerapplication_finalproject.component

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.repaircomputerapplication_finalproject.data.BottomNavigationBarList
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import com.example.repaircomputerapplication_finalproject.viewModel.HomeViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    var role = remember { mutableStateOf("") }
    val notificationCount by homeViewModel.notificationCount.collectAsState(initial = 0) // ตัวแปรเก็บจำนวนแจ้งเตือน

    LaunchedEffect(context) {
        role.value = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("role")] ?: ""
        }.first()
    }
    LaunchedEffect(notificationCount){
        homeViewModel.fetchNotifications()
    }
    NavigationBar {
        BottomNavigationBarList().bottomNavigation(role.value).forEach { navigationItem ->
            NavigationBarItem(
                selected = currentDestination == navigationItem.route,
                label = { Text(navigationItem.label) },
                icon = {
                    if (navigationItem.route == ScreenRoutes.Notification.route) {
                        Log.d(TAG, "BottomNavigationBar: ${notificationCount.toString()}")
                        // แสดง Badge เฉพาะที่ไอคอน "แจ้งเตือน"
                        BadgedBox(
                            badge = {
                                if (notificationCount > 0) {
                                    Badge { Text(text = notificationCount.toString()) }
                                }
                            }
                        ) {
                            Icon(navigationItem.icon, contentDescription = navigationItem.label)
                        }
                    } else {
                        Icon(navigationItem.icon, contentDescription = navigationItem.label)
                    }
                },
                onClick = {
                    if (navigationItem.route == ScreenRoutes.Notification.route) {

                        homeViewModel.resetNotificationCount() // รีเซ็ตจำนวนแจ้งเตือนเมื่อเข้าไปดู
                    }
                    navController.navigate(navigationItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

