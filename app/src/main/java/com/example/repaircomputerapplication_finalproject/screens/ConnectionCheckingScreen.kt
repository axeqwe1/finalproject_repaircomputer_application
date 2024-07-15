package com.example.repaircomputerapplication_finalproject.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.graph.RootNav
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionCheckingScreen(navController:NavController) {
    val apiService = RetrofitInstance.apiService
    var isConnected by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    suspend fun checkConnection(): Boolean {
        return try {
            val response = apiService.checkConnection()
            response.success
        } catch (e: Exception) {
            false
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            val result = checkConnection()
            if (result) {
                isConnected = true
                errorMessage = ""
            } else {
                isConnected = false
                errorMessage = "Failed to connect to the server"
            }
            delay(500) // 0.5 วิ
        }
    }
    if (errorMessage.isNotEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { coroutineScope.launch { checkConnection() } },
                modifier = Modifier.size(width = 275.dp, height = 50.dp),
            ) {
                Text("Retry")
            }
        }
    } else {
        if (isConnected) {
            RootNav()
        } else {
            CircularProgressIndicator()
        }
    }
}
