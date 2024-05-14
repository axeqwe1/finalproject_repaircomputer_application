package com.example.repaircomputerapplication_finalproject

import AddAdminScreen
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.repaircomputerapplication_finalproject.ui.theme.RepairComputerApplication_FinalProjectTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.compose.rememberNavController
import com.example.repaircomputerapplication_finalproject.graph.RootNav
import com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.displayEmployee


@OptIn(ExperimentalMaterial3Api::class,ExperimentalComposeUiApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            RepairComputerApplication_FinalProjectTheme {
                val keyboardController = LocalSoftwareKeyboardController.current
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface)
                {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE0F7FA))  //
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    keyboardController?.hide()
                                }
                            },
                        ) { //Column
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                RootNav()
//                                displayEmployee()
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RepairComputerApplication_FinalProjectTheme {
        Greeting("Android")
    }
}