package com.example.repaircomputerapplication_finalproject.component

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.repaircomputerapplication_finalproject.data.BottomNavigationBarList
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.viewModel.AuthViewModel
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import com.example.repaircomputerapplication_finalproject.viewModel.HomeViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDynamic(navController: NavController, scrollBehavior: TopAppBarScrollBehavior, homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    var role = remember { mutableStateOf("") }
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentStack = navBackStackEntry?.destination
    val isUpdate = remember { mutableStateOf(false) }

    if(currentStack?.route?.contains("isUpdate") == true){
        isUpdate.value = navBackStackEntry.arguments?.getBoolean("isUpdate") == true
    }
    LaunchedEffect(context) {
        role.value = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("role")] ?: ""
        }.first()
    }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    Log.d(TAG, "Current Route: $currentRoute")
    val screenTitle = getScreenTitle(currentRoute,isUpdate.value)

    TopAppBar(
        modifier = Modifier.background(Color.Transparent),
        title = {
            Text(
                text = screenTitle,
                color = Color.Black,
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val topNavBarDestination = BottomNavigationBarList().bottomNavigation(role.value).any { it.route == currentDestination?.route }
            Log.d(TAG, "TopAppBarDynamic: ${currentDestination?.route}")
            if (!topNavBarDestination) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Go Back", tint = Color.Black)
                }
            }
        },
        actions = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val topNavBarDestination = BottomNavigationBarList().bottomNavigation(role.value).any { it.route == currentDestination?.route }
            if (topNavBarDestination && (currentDestination?.route == ScreenRoutes.Menu.route || currentDestination?.route == ScreenRoutes.Dashboard.route)) {
                TextButton(onClick = { homeViewModel.logout() }) {
                    Text(text = "Logout", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

fun getScreenTitle(route: String,isUpdate:Boolean): String {
    return when {
        route.startsWith(ScreenRoutes.Home.route) -> "Home"
        route.startsWith(ScreenRoutes.Notification.route) -> "แจ้งเตือน"
        route.startsWith(ScreenRoutes.RequestRepairList.route) -> "รายการแจ้งซ่อม"
        route.startsWith(ScreenRoutes.LoginScreens.route) -> "ล็อคอิน"
        route.startsWith(ScreenRoutes.Menu.route) -> "หน้าเมนูหลัก"
        route.startsWith(ScreenRoutes.checkConnection.route) -> "Connecting....."
        route.startsWith(ScreenRoutes.ManageMenu.route) -> "Manage Menu"
        route.startsWith(ScreenRoutes.UserManageMenu.route) -> "เลือกจัดการข้อมูลผู้ใช้งาน"
        route.startsWith(ScreenRoutes.DataManageMenu.route) -> "เลือกจัดการข้อมูลทั่วไป"
        route.startsWith(ScreenRoutes.ManageUserScreen.route) -> "จัดการข้อมูลผู้ใช้งาน"
        route.startsWith(ScreenRoutes.ManageDataScreen.route) -> "จัดการข้อมูลทั่วไป"
        route.startsWith(ScreenRoutes.FormRequestForRepair.route) -> "แจ้งซ่อม"
        route.startsWith(ScreenRoutes.AddUserForm.route) -> "กรอกข้อมูลผู้ใช้งาน"
        route.startsWith(ScreenRoutes.DataForm.route) -> "กรอกข้อมูลทั่วไป"
        route.startsWith(ScreenRoutes.Report.route) -> "รายงาน"
        route.startsWith(ScreenRoutes.Dashboard.route) -> "Dashboard"
        route.startsWith(ScreenRoutes.detailRepair.route) -> "รายละเอียดแจ้งซ่อม"
        route.startsWith(ScreenRoutes.addDetailRepair.route) -> getAddDetailRepairTitle(route,isUpdate)
        route.startsWith(ScreenRoutes.statusDetail.route) -> "รายละเอียดสถานะการซ่อม"
        route.startsWith(ScreenRoutes.AssignWork.route) -> "จ่ายงาน"
        route.startsWith(ScreenRoutes.TechnicianListBacklog.route) -> "รายชื่อช่างเทคนิคสำหรับจ่ายงาน"
        else -> route // Default case to handle any unmatched routes
    }
}

fun getAddDetailRepairTitle(route: String,isUpdate: Boolean): String {

    // Use these values to return the appropriate title
    return if (isUpdate) {
        "แก้ไขข้อมูลของงาน"
    } else {
        "เพิ่มรายละเอียดงาน"
    }
}