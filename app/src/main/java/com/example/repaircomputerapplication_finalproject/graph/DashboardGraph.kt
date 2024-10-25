package com.example.repaircomputerapplication_finalproject.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.ConnectionCheckingScreen
import com.example.repaircomputerapplication_finalproject.screens.HomeScreen
import com.example.repaircomputerapplication_finalproject.screens.NotificationScreen
import com.example.repaircomputerapplication_finalproject.screens.reportscreen.DashboardScreen
import com.example.repaircomputerapplication_finalproject.screens.reportscreen.ReportListScreen



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashBoardHost(navController: NavHostController){
    NavHost(navController = navController,
        startDestination = ScreenRoutes.DashboardNav.route
    )   {
        dashBoard(navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.dashBoard(navController: NavHostController){
    navigation(route = ScreenRoutes.DashboardNav.route, startDestination = ScreenRoutes.Dashboard.route){
        composable(route = ScreenRoutes.Dashboard.route)
        {
            DashboardScreen(navController = navController)
        }
        composable(route = ScreenRoutes.Report.route){
            ReportListScreen(navController = navController)
        }
        composable(route = ScreenRoutes.checkConnection.route)
        {
            ConnectionCheckingScreen(navController)
        }
    }
}