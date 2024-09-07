package com.example.repaircomputerapplication_finalproject.screens

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
//import androidx.compose.material.BottomNavigation
//import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.repaircomputerapplication_finalproject.component.BottomNavigationBar
import com.example.repaircomputerapplication_finalproject.component.TopAppBarDynamic
import com.example.repaircomputerapplication_finalproject.data.BottomNavigationBarList
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.graph.DashBoardHost
import com.example.repaircomputerapplication_finalproject.graph.MenuNavGraph
import com.example.repaircomputerapplication_finalproject.viewModel.HomeViewModel
import com.example.repaircomputerapplication_finalproject.viewModel.LogoutResult
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val navHostController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val logoutResult by homeViewModel.logoutResult.collectAsState(initial = null)
    val context = LocalContext.current
    var role = remember { mutableStateOf("") }

    LaunchedEffect(context) {
        role.value = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("role")] ?: ""
        }.first()
    }
    LaunchedEffect(logoutResult) {
        when (logoutResult) {
            is LogoutResult.IsLogout -> navController.navigate(ScreenRoutes.AuthNav.route) {
                popUpTo(ScreenRoutes.HomeNav.route) { inclusive = true }
            }
            is LogoutResult.Failure -> Toast.makeText(context, "Login failed: ${(logoutResult as LogoutResult.Failure).error}", Toast.LENGTH_LONG).show()
            else -> {}
        }
    }

    println(navController.currentBackStackEntryAsState().value?.destination?.route)

    // ใช้ Box ที่มี background gradient

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBarDynamic(navHostController, scrollBehavior, homeViewModel)
            },
            bottomBar = {
                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val shouldShowBottomBar = BottomNavigationBarList().bottomNavigation(role.value).any { it.route == currentDestination?.route }
                Box(modifier = Modifier.fillMaxWidth()) {
                    AnimatedVisibility(
                        visible = shouldShowBottomBar,
                        enter = fadeIn(animationSpec = tween(durationMillis = 700)),
                        exit = shrinkOut(animationSpec = tween(durationMillis = 700)),
                    ) {
                        BottomAppBar(
                            containerColor = Color.Transparent,
                            contentColor = Color.Transparent,
                        ) {}
                        BottomNavigationBar(navHostController)
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color(0xFFB3E5FC)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // ส่วนของเนื้อหาหน้า Home
                if(role.value != "Chief"){
                    MenuNavGraph(navHostController)
                }else{
                    DashBoardHost(navHostController)
                }


                // Put UI Here
            }
        }
    }



