package com.example.repaircomputerapplication_finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NotificationScreen(){
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
                        androidx.wear.compose.material.Text("$index \n")
                        androidx.wear.compose.material.Text("Test \n")
                        androidx.wear.compose.material.Text("Number \n")
                    }
                }
            }
        }
    }
}