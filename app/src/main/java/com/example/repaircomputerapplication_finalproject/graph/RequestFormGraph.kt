package com.example.repaircomputerapplication_finalproject.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import formRequestForRepair


fun NavGraphBuilder.requestFormGraph(navController: NavController){
    navigation(route = ScreenRoutes.RequestFormNav.route, startDestination = ScreenRoutes.FormRequestForRepair.route){
        composable(route = ScreenRoutes.FormRequestForRepair.route){
            formRequestForRepair(navController)
        }
    }
}