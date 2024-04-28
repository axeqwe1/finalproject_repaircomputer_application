package com.example.repaircomputerapplication_finalproject.screens

import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen (navigator: NavController){
    var Email by remember { mutableStateOf("") }
    var Password by remember { mutableStateOf("") }
    val toast = Toast.makeText(LocalContext.current,"test", Toast.LENGTH_SHORT)
    val keyboardController = LocalSoftwareKeyboardController.current


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
            onClick = {
                navigator.navigate("MenuScreen")
            },
            modifier = Modifier.size(width = 275.dp, height = 50.dp),

        ){
            Text("Login")
        }
    }
}

