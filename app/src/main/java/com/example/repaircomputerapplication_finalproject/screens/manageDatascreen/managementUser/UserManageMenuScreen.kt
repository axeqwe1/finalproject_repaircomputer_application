package com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.managementUser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes


@Composable
fun  userManageMenuScreen(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        UserMenuList().userMenuList().forEach{
            item ->
            Spacer(modifier = Modifier.padding(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(20.dp,RoundedCornerShape(20))
                    .clip(RoundedCornerShape(20))
                    .background(color = Color.Yellow)
                    .clickable { navController.navigate(item.Route) }
                    .size(120.dp),
                contentAlignment = Alignment.Center
            ){
                Text(text = item.Btn_Name, color = Color.Black, fontSize = 24.sp)
            }
        }
    }
}



data class UserMenuList(
    val Btn_Name : String = "",
    val Icon : ImageVector = Icons.AutoMirrored.Filled.List,
    val Route : String = ""
){
    fun userMenuList(): List<UserMenuList>{
        var listMenu = listOf<UserMenuList>()
        listMenu = listOf(
            UserMenuList(
                 Btn_Name  = "จัดการข้อมูลแอดมิน",
                 Icon  = Icons.Default.Warning,
                 Route  = ScreenRoutes.ManageUserScreen.passUserType("Admin")
            ),
            UserMenuList(
                Btn_Name  = "จัดการข้อมูลช่าง",
                Icon  = Icons.Default.Warning,
                Route  = ScreenRoutes.ManageUserScreen.passUserType("Technician")
            ),
            UserMenuList(
                Btn_Name  = "จัดการข้อมูลพนักงาน",
                Icon  = Icons.Default.Warning,
                Route  = ScreenRoutes.ManageUserScreen.passUserType("Employee")
            ),
            UserMenuList(
                Btn_Name  = "จัดการข้อมูลหัวหน้า",
                Icon  = Icons.Default.Warning,
                Route  = ScreenRoutes.ManageUserScreen.passUserType("Chief")
            )
        )
        return listMenu
    }
}