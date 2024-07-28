package com.example.repaircomputerapplication_finalproject.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.ConnectionCheckingScreen
import com.example.repaircomputerapplication_finalproject.screens.NotificationScreen
import com.example.repaircomputerapplication_finalproject.screens.reportscreen.DashboardScreen

fun NavGraphBuilder.dashBoard(navController: NavHostController){
    navigation(route = ScreenRoutes.DashboardNav.route, startDestination = ScreenRoutes.Dashboard.route){
        composable(route = ScreenRoutes.Dashboard.route)
        {
            DashboardScreen(navController = navController)
        }
        composable(route = ScreenRoutes.checkConnection.route)
        {
            ConnectionCheckingScreen(navController)
        }
    }
}