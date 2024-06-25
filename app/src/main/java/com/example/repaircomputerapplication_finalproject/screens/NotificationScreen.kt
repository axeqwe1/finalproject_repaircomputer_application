package com.example.repaircomputerapplication_finalproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.repaircomputerapplication_finalproject.viewModel.NotificationViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun NotificationScreen(viewModel: NotificationViewModel = viewModel()){
    val vmodel = viewModel
    val notiList = vmodel.notiList.collectAsState().value ?: emptyList()
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = true) {
        delay(2000) // Delay for 3 seconds
        isLoading = false
    }
    if(isLoading){
        LoadingScreen()
    }else{
        if(notiList.isEmpty()){
            Text(text = "ไม่มีการแจ้งเตือน")
        }
        else{
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(notiList){ item ->
                        OutlinedCard(modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                        ){
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Gray)
                                .padding(16.dp)
                            ){
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    androidx.wear.compose.material.Text("${item.noti_message} \n")
                                    androidx.wear.compose.material.Text("${item.timestamp} \n")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}