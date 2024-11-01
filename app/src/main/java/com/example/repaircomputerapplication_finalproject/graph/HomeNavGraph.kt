package com.example.repaircomputerapplication_finalproject.graph

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
//import androidx.compose.ui.tooling.data.EmptyGroup.name
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.repaircomputerapplication_finalproject.component.MenuScreen
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.ConnectionCheckingScreen
import com.example.repaircomputerapplication_finalproject.screens.HomeScreen
import com.example.repaircomputerapplication_finalproject.screens.LoginScreen
import com.example.repaircomputerapplication_finalproject.screens.NotificationScreen
import com.example.repaircomputerapplication_finalproject.screens.RequestRepairScreen


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeGraph(navController: NavHostController){
    navigation(
        startDestination = ScreenRoutes.Home.route,
        route = ScreenRoutes.HomeNav.route
    ) {
        composable(route = ScreenRoutes.Home.route)
        {
            HomeScreen(navController)
        }
        composable(route = ScreenRoutes.checkConnection.route)
        {
            ConnectionCheckingScreen(navController)
        }
    }
}