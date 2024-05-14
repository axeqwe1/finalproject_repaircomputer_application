package com.example.repaircomputerapplication_finalproject.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.BottomNavigation
//import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Icon
import com.example.repaircomputerapplication_finalproject.BottomNavigationBar
import com.example.repaircomputerapplication_finalproject.component.Lastest_List
import com.example.repaircomputerapplication_finalproject.component.MenuScreen
import com.example.repaircomputerapplication_finalproject.component.Menu_List
import com.example.repaircomputerapplication_finalproject.component.TopAppBarDynamic
import com.example.repaircomputerapplication_finalproject.data.BottomNavigationBarList
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.graph.MenuNavGraph
import com.example.repaircomputerapplication_finalproject.graph.RootNav
import com.example.repaircomputerapplication_finalproject.viewModel.HomeViewModel
import com.example.repaircomputerapplication_finalproject.viewModel.LogoutResult
import com.example.repaircomputerapplication_finalproject.viewModel.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (nav:NavController,homeViewModel: HomeViewModel = viewModel()){
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val logoutResult by homeViewModel.logoutResult.collectAsState(initial = null)
    val context = LocalContext.current
    val dataStore = context.dataStore


    LaunchedEffect(logoutResult) {
        val sessionId =  dataStore.data.firstOrNull()?.get(stringPreferencesKey("session_key"))
        Log.d(TAG,"Session ID $sessionId " ?: "No session ID found")
        when(logoutResult){
            is LogoutResult.IsLogout -> nav.navigate(ScreenRoutes.AuthNav.route){
                popUpTo(ScreenRoutes.HomeNav.route){ inclusive = true }
            }
            is LogoutResult.Failure -> Toast.makeText(context, "Login failed: ${(logoutResult as LogoutResult.Failure).error}", Toast.LENGTH_LONG).show()
            else -> {}
        }
    }

    println(navController.currentBackStackEntryAsState().value?.destination?.route)
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBarDynamic(navController,scrollBehavior,homeViewModel)
             },
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val shouldShowBottomBar = BottomNavigationBarList().bottomNavigation().any() {it.route == currentDestination?.route}
                Box(modifier = Modifier.fillMaxWidth()){
                    AnimatedVisibility(
                        visible = shouldShowBottomBar,
                        enter = fadeIn(animationSpec = tween(durationMillis = 700)),
                        exit = shrinkOut(animationSpec = tween(durationMillis = 700)),
                    ){
                        BottomAppBar(
                            containerColor = Color.Transparent,
                            contentColor = Color.Transparent,
                        ){}

                        BottomNavigationBar(navController)
                    }
                }
                }
            )
        {
           innerPading ->
            Column(
                modifier = Modifier.padding(innerPading).background(Color(0xFFE0F7FA)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                    // ส่วนของเนื้อหาหน้า Home
                    MenuNavGraph(navController)

                //Put UI Here
            }
        }
}


