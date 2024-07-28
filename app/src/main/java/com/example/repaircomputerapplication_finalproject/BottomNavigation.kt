package com.example.repaircomputerapplication_finalproject

import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.repaircomputerapplication_finalproject.data.BottomNavigationBarList
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    var role = remember { mutableStateOf("") }

    LaunchedEffect(context) {
        role.value = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("role")] ?: ""
        }.first()
    }
    Log.d(TAG, "BottomNavigationBar: ${navController.currentBackStackEntryAsState().value?.destination?.route}")
    NavigationBar {
        BottomNavigationBarList().bottomNavigation(role.value).forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = currentDestination == navigationItem.route,
                label = { Text(navigationItem.label) },
                icon = { Icon(navigationItem.icon, contentDescription = navigationItem.label) },
                onClick = {
                    navController.navigate(navigationItem.route)
                    {
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

