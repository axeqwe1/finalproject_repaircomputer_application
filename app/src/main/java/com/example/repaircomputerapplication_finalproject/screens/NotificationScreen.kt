package com.example.repaircomputerapplication_finalproject.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.repaircomputerapplication_finalproject.viewModel.NotificationViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.repaircomputerapplication_finalproject.`api-service`.ConnectionChecker
import com.example.repaircomputerapplication_finalproject.utils.formatTimestamp
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationScreen(viewModel: NotificationViewModel = viewModel()) {
    val notiList = viewModel.notiList.collectAsState().value ?: emptyList()
    var isLoading by remember { mutableStateOf(true) }
    var showCount by remember { mutableStateOf(5) } // Initial count of items to show

    LaunchedEffect(Unit) {
        if (ConnectionChecker.checkConnection()) {
            isLoading = false
        }
    }

    if (isLoading) {
        LoadingScreen()
    } else {
        if (notiList.isEmpty()) {
            Text(text = "ไม่มีการแจ้งเตือน")
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    val itemsToShow = notiList.take(showCount) // Show only the items up to showCount
                    items(itemsToShow) { item ->
                        OutlinedCard(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp) // Add rounded corners
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(16.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    item.noti_message?.let {
                                        Text(
                                            text = it,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                    }
                                    Text(
                                        text = formatTimestamp(item.timestamp),
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    // "แสดงเพิ่มเติม" button
                    if (showCount < notiList.size) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "แสดงเพิ่มเติม",
                                    color = Color.Blue,
                                    modifier = Modifier
                                        .clickable { showCount += 5 } // Increase the count by 5 on each click
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

