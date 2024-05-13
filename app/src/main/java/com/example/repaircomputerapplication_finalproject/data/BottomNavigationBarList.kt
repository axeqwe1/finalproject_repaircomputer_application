package com.example.repaircomputerapplication_finalproject.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext

data class BottomNavigationBarList(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = "",
){
    fun bottomNavigation() : List<BottomNavigationBarList>{
        return listOf(
            BottomNavigationBarList(
                label = "Home",
                icon = Icons.Filled.Home,
                route = ScreenRoutes.Menu.route
            ),
            BottomNavigationBarList(
                label = "แจ้งเตือน",
                icon =  Icons.Filled.Notifications,
                route = ScreenRoutes.Notification.route
            ),
            BottomNavigationBarList(
                label = "รายการแจ้งซ่อม",
                icon =  Icons.Filled.List,
                route = ScreenRoutes.RequestRepairList.route
            )
        )
    }
}