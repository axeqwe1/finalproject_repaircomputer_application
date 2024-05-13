package com.example.repaircomputerapplication_finalproject.graph

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
//import androidx.compose.ui.tooling.data.EmptyGroup.name
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.repaircomputerapplication_finalproject.component.MenuScreen
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.HomeScreen
import com.example.repaircomputerapplication_finalproject.screens.LoginScreen
import com.example.repaircomputerapplication_finalproject.screens.NotificationScreen
import com.example.repaircomputerapplication_finalproject.screens.RequestRepairScreen


fun NavGraphBuilder.homeGraph(navController: NavHostController){
    navigation(
        startDestination = ScreenRoutes.Home.route,
        route = ScreenRoutes.HomeNav.route
    ) {
        composable(route = ScreenRoutes.Home.route)
        {
            HomeScreen(navController)
        }
    }
}