package com.example.repaircomputerapplication_finalproject.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.repaircomputerapplication_finalproject.component.MenuScreen
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.graph.AssignWorkGraph.assignWorkGraph
import com.example.repaircomputerapplication_finalproject.graph.DisplayGraph.detailGraph
import com.example.repaircomputerapplication_finalproject.graph.ManagementGraph.manageMenuDataGraph
import com.example.repaircomputerapplication_finalproject.screens.NotificationScreen
import com.example.repaircomputerapplication_finalproject.screens.RequestRepairScreen

@Composable
fun MenuNavGraph(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        route = ScreenRoutes.MenuNav.route,
        startDestination = ScreenRoutes.Menu.route
    ){
        composable(route = ScreenRoutes.Menu.route)
        {
            MenuScreen(navController)
        }
        RequestListGraph(navController)
        NotiGraph(navController)
        requestFormGraph(navController)
        manageMenuDataGraph(navController)
        assignWorkGraph(navController)
    }
}
fun NavGraphBuilder.RequestListGraph(navController: NavHostController){
    navigation(route = ScreenRoutes.ListRequestNav.route, startDestination = ScreenRoutes.RequestRepairList.route){
        composable(route = ScreenRoutes.RequestRepairList.route){
            RequestRepairScreen(navController)
        }
        detailGraph(navController)
    }
}
fun NavGraphBuilder.NotiGraph(navController: NavHostController){
    navigation(route = ScreenRoutes.NotiNav.route, startDestination = ScreenRoutes.Notification.route){
        composable(route = ScreenRoutes.Notification.route)
        {
            NotificationScreen()
        }
    }
}

