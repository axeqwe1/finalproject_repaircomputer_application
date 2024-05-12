package com.example.repaircomputerapplication_finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text

@Composable
fun RequestRepairScreen(navController: NavHostController){
    Column(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.Text(
                modifier = Modifier.padding(24.dp),
                text = "SearchBarPlace",
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.Text(
                modifier = Modifier.padding(24.dp),
                text = "SelectDateBtnPlace",
                fontWeight = FontWeight.Bold
            )
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(4){
                    index ->
                OutlinedCard(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                ){
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .padding(24.dp)
                    ){
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("$index \n")
                            Text("Test \n")
                            Text("Number \n")
                        }
                    }
                }
            }
        }
    }
}