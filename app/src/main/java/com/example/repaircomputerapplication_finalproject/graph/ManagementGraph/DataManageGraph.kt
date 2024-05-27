package com.example.repaircomputerapplication_finalproject.graph.ManagementGraph

import com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.managementData.dataManageMenuScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.form.formManageData.DataForm
import com.example.repaircomputerapplication_finalproject.screens.form.formManageData.crudDataScreen
import com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.managementUser.crudUserScreen

fun NavGraphBuilder.dataManageGraph(navController: NavHostController){
    navigation(route = ScreenRoutes.DataManageMenuNav.route, startDestination = ScreenRoutes.DataManageMenu.route){
        composable(route = ScreenRoutes.DataManageMenu.route)
        {
            dataManageMenuScreen(navController)
        }
        composable(
            route = ScreenRoutes.ManageDataScreen.route,
            arguments = listOf(
                navArgument("DataType"){
                    type = NavType.StringType
                    nullable = true
                },
            )
        ){ backStackEntry ->
            val dataType = backStackEntry.arguments?.getString("DataType")
            crudDataScreen(dataType,navController)
        }
        composable(
            route = ScreenRoutes.DataForm.route,
            arguments = listOf(
                navArgument("DataType"){
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("DataID"){
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("isEdit"){
                    type = NavType.BoolType
                    nullable = false
                }
            )
        ){ navBackStackEntry ->
            val DataType = navBackStackEntry.arguments?.getString("DataType")
            val DataID = navBackStackEntry.arguments?.getString("DataID")
            val isEdit = navBackStackEntry.arguments?.getBoolean("isEdit")

            DataForm(DataType = DataType, isEdit = isEdit, DataID = DataID, navController = navController)
        }
    }
}