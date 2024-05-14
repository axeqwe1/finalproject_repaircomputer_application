package com.example.repaircomputerapplication_finalproject.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.NotificationScreen
import com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.MenuManagement
import com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.manageAdminScreen

fun NavGraphBuilder.manageMenuDataGraph(navController: NavHostController){
    navigation(route = ScreenRoutes.ManageMenuNav.route, startDestination = ScreenRoutes.ManageMenu.route){
        composable(route = ScreenRoutes.ManageMenu.route)
        {
            MenuManagement()
        }
        composable(route = ScreenRoutes.ManageAdminScreen.route){
            manageAdminScreen()
        }
    }
}