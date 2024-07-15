package com.example.repaircomputerapplication_finalproject.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.viewModel.AuthViewModel
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen (navController: NavController,loginViewModel: AuthViewModel = viewModel()){
    var Email by remember { mutableStateOf("") }
    var Password by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val loginResult by loginViewModel.loginResult.collectAsState(initial = null)
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(true) }
    var isLogin = remember { mutableStateOf(false) }


    // UI reacts to state changes
    LaunchedEffect(loginResult) {
        isLogin.value = context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey("isLogin")] ?: false
        }.first() // ดึงค่า isLogin และรอจนกว่าจะได้ผลลัพธ์
        if (isLogin.value) {
            isLoading.value = false
            navController.navigate(ScreenRoutes.HomeNav.route) {
                popUpTo(ScreenRoutes.AuthNav.route) { inclusive = true }
            }
        }
        isLoading.value = false
    }

    if(isLoading.value){
        LoadingScreen()
    }else{
    if(!isLogin.value){
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = Email,
                    onValueChange = {Email=it},
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { keyboardController?.hide() }
                    )
                )
                OutlinedTextField(
                    value = Password,
                    onValueChange = {Password=it},
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { keyboardController?.hide() }
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                )
                Box(modifier = Modifier.padding(16.dp))
                OutlinedButton(
    //            onClick = {navController.navigate(ScreenRoutes.HomeNav.route)},
                    onClick = {loginViewModel.login(Email,Password)},
                    modifier = Modifier.size(width = 275.dp, height = 50.dp),
                ){
                    Text("Login")
                }
            }
        }
    }
}


