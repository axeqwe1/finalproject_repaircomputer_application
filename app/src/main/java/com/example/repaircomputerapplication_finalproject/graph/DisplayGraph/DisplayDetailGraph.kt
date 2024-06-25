package com.example.repaircomputerapplication_finalproject.graph.DisplayGraph

import DetailRepairScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.form.formAddDetail

fun NavGraphBuilder.detailGraph(navController: NavHostController){
    navigation(
        startDestination = ScreenRoutes.detailRepair.route,
        route = ScreenRoutes.DisplayNav.route
    ){
        composable(
            route = ScreenRoutes.detailRepair.route,
            arguments = listOf(
                navArgument("rrid"){
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("userType"){
                    type = NavType.StringType
                    nullable = false
                }
            )
        ){ navBackStackEntry ->
            val rrid = navBackStackEntry.arguments?.getString("rrid")
            val userType = navBackStackEntry.arguments?.getString("userType")
            DetailRepairScreen(rrid ?: "null",userType ?: "null",navController)
        }
        composable(
            route = ScreenRoutes.addDetailRepair.route,
            arguments = listOf(
                navArgument("rrid"){
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("isUpdate"){
                    type = NavType.BoolType
                    nullable = false
                },
                navArgument("rd_id"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ){ navBackStackEntry ->
            val rrid = navBackStackEntry.arguments?.getString("rrid")
            val isUpdate = navBackStackEntry.arguments?.getBoolean("isUpdate")
            val rd_id = navBackStackEntry.arguments?.getString("rd_id")
            formAddDetail(rrid ?: "null",rd_id ?: "null",isUpdate ?: false,navController)
        }
    }
}