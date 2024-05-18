package com.example.repaircomputerapplication_finalproject.graph.ManagementGraph



import AddUserForm
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.managementUser.crudUserScreen
import com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.managementUser.userManageMenuScreen

fun NavGraphBuilder.userManageMenuGraph(navController: NavHostController){
    navigation(route = ScreenRoutes.UserManageMenuNav.route, startDestination = ScreenRoutes.UserManageMenu.route){
        composable(route = ScreenRoutes.UserManageMenu.route)
        {
            userManageMenuScreen(navController)
        }
        composable(
            route = ScreenRoutes.ManageUserScreen.route,
            arguments = listOf(
                navArgument("UserType"){
                    type = NavType.StringType
                    nullable = true
                },
            )
        ){ backStackEntry ->
            val userType = backStackEntry.arguments?.getString("UserType")
            crudUserScreen(userType,navController)
        }
        composable(
            route = ScreenRoutes.AddUserForm.route,
            arguments = listOf(
                navArgument("UserType"){
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("isEdit"){
                    type = NavType.BoolType
                    nullable = false
                },
                navArgument("UserId"){
                    type = NavType.StringType
                    nullable = true
                },
            )
        ){ backStackEntry ->
            val userType = backStackEntry.arguments?.getString("UserType")
            val isEdit = backStackEntry.arguments?.getBoolean("isEdit")
            val userId = backStackEntry.arguments?.getString("UserId")
            AddUserForm(userType,isEdit,userId)
        }
    }
}