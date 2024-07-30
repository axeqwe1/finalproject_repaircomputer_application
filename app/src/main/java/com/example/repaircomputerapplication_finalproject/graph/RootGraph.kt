package com.example.repaircomputerapplication_finalproject.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.HomeScreen
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNav(){
    val context = LocalContext.current
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf(ScreenRoutes.AuthNav.route) }
    NavHost(navController = navController, startDestination =  startDestination){
        AuthNav(navController)
        homeGraph(navController)
    }
}