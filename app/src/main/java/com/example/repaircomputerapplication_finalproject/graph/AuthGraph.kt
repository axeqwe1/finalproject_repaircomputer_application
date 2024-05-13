package com.example.repaircomputerapplication_finalproject.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.LoginScreen

fun NavGraphBuilder.AuthNav(navController: NavHostController)
{
    navigation(
        startDestination = ScreenRoutes.LoginScreens.route,
        route = ScreenRoutes.AuthNav.route
    ){
        composable(route = ScreenRoutes.LoginScreens.route){
            LoginScreen(navController)
        }
    }
}
