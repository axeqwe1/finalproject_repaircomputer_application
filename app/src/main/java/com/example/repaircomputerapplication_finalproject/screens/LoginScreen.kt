package com.example.repaircomputerapplication_finalproject.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.viewModel.AuthViewModel
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavController, loginViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val loginResult by loginViewModel.loginResult.collectAsState(initial = null)
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(true) }
    val isLogin = remember { mutableStateOf(false) }
    val role = remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    // UI reacts to state changes
    LaunchedEffect(loginResult) {
        isLogin.value = context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey("isLogin")] ?: false
        }.first() // ดึงค่า isLogin และรอจนกว่าจะได้ผลลัพธ์
        role.value = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("role")] ?: ""
        }.first()
        if (isLogin.value) {
            isLoading.value = false
            if (role.value == "Chief") {
                navController.navigate(ScreenRoutes.HomeNav.route) {
                    popUpTo(ScreenRoutes.AuthNav.route) { inclusive = true }
                }
            } else {
                navController.navigate(ScreenRoutes.HomeNav.route) {
                    popUpTo(ScreenRoutes.AuthNav.route) { inclusive = true }
                }
            }
        }
        isLoading.value = false
    }

    LaunchedEffect(loginResult) {
        loginResult?.let { result ->
            if (result == false) {
                dialogMessage = "Email หรือ รหัสผ่านไม่ถูกต้อง."
                showDialog = true
            }
        }
    }

    if (isLoading.value) {
        LoadingScreen()
    } else {
        if (!isLogin.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "ระบบแจ้งซ่อมคอมพิวเตอร์ Online",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { keyboardController?.hide() }
                    )
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { keyboardController?.hide() }
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                )
                Box(modifier = Modifier.padding(16.dp))
                OutlinedButton(
                    onClick = {
                        if (email.isEmpty() || password.isEmpty()) {
                            dialogMessage = "กรุณากรอก Email และ รหัสผ่าน"
                            showDialog = true
                        } else {
                            loginViewModel.login(email, password)
                            Log.d(TAG, "LoginScreen: $loginResult")
                        }
                    },
                    modifier = Modifier.size(width = 275.dp, height = 50.dp),
                ) {
                    Text("Login")
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Login Failed") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
