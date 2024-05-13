package com.example.repaircomputerapplication_finalproject.graph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.HomeScreen

@Composable
fun RootNav(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination =  ScreenRoutes.AuthNav.route){
        AuthNav(navController)
        homeGraph(navController)
    }
}