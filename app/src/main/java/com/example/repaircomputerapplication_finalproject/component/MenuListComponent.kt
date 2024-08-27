package com.example.repaircomputerapplication_finalproject.component

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.repaircomputerapplication_finalproject.data.NavigateListForBtn
import com.example.repaircomputerapplication_finalproject.model.AdminData
import com.example.repaircomputerapplication_finalproject.model.ChiefData
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.TechnicianData
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import com.example.repaircomputerapplication_finalproject.viewModel.HomeViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@Composable
fun MenuScreen(navController: NavController){
    //Put UI Here
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.padding(28.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
            contentAlignment = Alignment.Center){
            HeroSection()
        }
        Box(modifier = Modifier.padding(24.dp))
        {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    androidx.compose.material3.Text(
                        modifier = Modifier.padding(0.dp),
                        text = "เมนูลัด",
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                    contentAlignment = Alignment.Center){
                    Menu_List(navController)
                }

            }
        }
    }
}

@Composable
fun HeroSection(homeViewModel: HomeViewModel = viewModel()) {
    var userId by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var employeeData by remember { mutableStateOf<EmployeeData?>(null) }
    var technicianData by remember { mutableStateOf<TechnicianData?>(null) }
    var adminData by remember { mutableStateOf<AdminData?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        userId = context.dataStore.data.map { item ->
            item[stringPreferencesKey("userId")]
        }.first() ?: "null"

        role = context.dataStore.data.map { item ->
            item[stringPreferencesKey("role")]
        }.first() ?: "null"

        if (userId != "null") {
            when(role) {
                "Employee" -> {
                    homeViewModel.viewModelScope.launch {
                        employeeData = homeViewModel.getEmployeeData(userId.toInt())
                    }
                }
                "Technician" -> {
                    homeViewModel.viewModelScope.launch {
                        technicianData = homeViewModel.getTechnicianData(userId.toInt())
                    }
                }
                "Admin" -> {
                    homeViewModel.viewModelScope.launch {
                        adminData = homeViewModel.getAdminData(userId.toInt())
                    }
                }
            }
        }
    }
    // ชื่อผู้ใช้งาน
    val name = when(role) {
        "Employee" -> employeeData?.firstname
        "Technician" -> technicianData?.firstname
        "Admin" -> adminData?.firstname
        else -> "Loading..."
    }
    val lastname = when(role) {
        "Employee" -> employeeData?.lastname
        "Technician" -> technicianData?.lastname
        "Admin" -> adminData?.lastname
        else -> "Loading..."
    }
    val roleThaiName = when(role){
        "Employee" -> "พนักงาน"
        "Technician" -> "ช่างเทคนิค"
        "Admin" -> "Admin"
        else -> "Loading..."
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .size(width = 175.dp, height = 275.dp)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .size(width = 175.dp, height = 275.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ชื่อผู้ใช้งาน
            Text(
                text = "$name $lastname",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
            // ตำแหน่งผู้ใช้งาน
            Text(
                text = "ตำแหน่ง : $roleThaiName",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

//            // ส่วนแสดงจำนวน งานค้าง, จำนวนงานที่เสร็จ, งานที่รับทั้งหมด
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//
//            }
        }
    }
}

//@Composable
//fun ProfileStatItem(value: String, label: String) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = value,
//            fontWeight = FontWeight.Bold,
//            fontSize = 18.sp,
//            color = Color.Black
//        )
//        Text(
//            text = label,
//            fontSize = 14.sp,
//            color = Color.Gray
//        )
//    }
//}

@Composable
fun Menu_List(navController: NavController) {
    val context = LocalContext.current
    val role = remember { mutableStateOf("") }
    LaunchedEffect(key1 = true) {
        val dataStore = context.dataStore
        role.value = dataStore.data.map { items ->
            items[stringPreferencesKey("role")] ?: ""
        }.first()
    }
    Log.d(TAG, "Menu_List: $role")
    val navigateList = NavigateListForBtn().btnNavigate(role.value)
    LazyHorizontalGrid(rows = GridCells.Fixed(1)) {
        items(navigateList.size) { index ->
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp), clip = false)
                    .clip(RoundedCornerShape(15))
                    .clickable {
                        navController.navigate(navigateList[index].Route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    .background(Color.Cyan)
                    .size(width = 125.dp, height = 125.dp),
                contentAlignment = Alignment.Center // จัดให้อยู่ตรงกลาง
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp), // เพิ่ม padding ให้กับเนื้อหาในปุ่ม
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    androidx.wear.compose.material.Icon(
                        imageVector = navigateList[index].Icon,
                        contentDescription = navigateList[index].Btn_Name,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = navigateList[index].Btn_Name,
                        modifier = Modifier.fillMaxWidth(), // ทำให้ Text ครอบคลุมพื้นที่เต็มความกว้าง
                        textAlign = TextAlign.Center // จัดข้อความให้อยู่ตรงกลาง
                    )
                }
            }
        }
    }
}