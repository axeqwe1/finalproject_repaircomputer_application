package com.example.repaircomputerapplication_finalproject.screens.manageDatascreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes


@Composable
fun MenuManagement(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize().padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(20.dp,RoundedCornerShape(20))
                .clip(RoundedCornerShape(20))
                .clickable {
                    navController.navigate(ScreenRoutes.UserManageMenu.route)
                }
                .background(Color.Yellow)
                .size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "จัดการข้อมูลผู้ใช้งาน", color = Color.Black, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(20.dp,RoundedCornerShape(20))
                .clip(RoundedCornerShape(20))
                .clickable {
                    navController.navigate(ScreenRoutes.DataManageMenu.route)
                }
                .background(Color.Yellow)
                .size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "จัดการข้อมูลทั่วไป", color = Color.Black, fontSize = 24.sp)
        }
    }
}