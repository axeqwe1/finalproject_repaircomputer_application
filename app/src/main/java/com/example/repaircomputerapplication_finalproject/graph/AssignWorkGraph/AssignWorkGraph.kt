package com.example.repaircomputerapplication_finalproject.graph.AssignWorkGraph

import com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.managementData.dataManageMenuScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.assignScreen.displayRequestListForAssign
import com.example.repaircomputerapplication_finalproject.screens.assignScreen.technicianListBacklogScreen
import com.example.repaircomputerapplication_finalproject.screens.form.formManageData.DataForm
import com.example.repaircomputerapplication_finalproject.screens.form.formManageData.crudDataScreen
import com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.managementUser.crudUserScreen

fun NavGraphBuilder.assignWorkGraph(navController: NavHostController){
    navigation(route = ScreenRoutes.AssignWorkNav.route, startDestination = ScreenRoutes.AssignWork.route){
        composable(route = ScreenRoutes.AssignWork.route)
        {
            displayRequestListForAssign(navController)
        }
        composable(
            route = ScreenRoutes.TechnicianListBacklog.route,
            arguments = listOf(
                navArgument("admin_id"){
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("rrid"){
                    type = NavType.StringType
                    nullable = false
                }
            )
        )
        { navBackStackEntry ->
            val admin_id = navBackStackEntry.arguments?.getString("admin_id")
            val rrid = navBackStackEntry.arguments?.getString("rrid")
            technicianListBacklogScreen(
                navController = navController,
                admin_id = admin_id ?: "null",
                rrid = rrid ?: "null"
            )
        }
    }
}