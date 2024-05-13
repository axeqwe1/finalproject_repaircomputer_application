package com.example.repaircomputerapplication_finalproject.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.repaircomputerapplication_finalproject.data.BottomNavigationBarList
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.viewModel.AuthViewModel
import com.example.repaircomputerapplication_finalproject.viewModel.HomeViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDynamic(navController: NavController,scrollBehavior: TopAppBarScrollBehavior,homeViewModel: HomeViewModel) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    TopAppBar(
        title = { Text(
            text = currentRoute,
            color = Color.Black,
            fontSize = 24.sp
        ) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface
        ),
        navigationIcon = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val topNavBarDestination = BottomNavigationBarList().bottomNavigation().any() {it.route == currentDestination?.route}
            if(!topNavBarDestination){
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back", tint = Color.Black)
                }
            }
        },
        actions = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val topNavBarDestination = BottomNavigationBarList().bottomNavigation().any() {it.route == currentDestination?.route}
            if(topNavBarDestination && currentDestination?.route == ScreenRoutes.Menu.route){
                TextButton(onClick = {homeViewModel.logout()}) {
                    Text(text = "Logout", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

