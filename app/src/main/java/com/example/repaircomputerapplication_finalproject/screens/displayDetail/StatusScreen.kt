package com.example.repaircomputerapplication_finalproject.screens.displayDetail

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.repaircomputerapplication_finalproject.viewModel.DisplayViewModel.RepairDetailViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.repaircomputerapplication_finalproject.viewModel.formatTimestamp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatusScreen(rrid: String?, viewModel: RepairDetailViewModel = viewModel()) {
    val data = viewModel.detailData.collectAsState().value
    var rr_date by remember { mutableStateOf("") }
    var rr_status by remember { mutableStateOf("") }

    LaunchedEffect(data) {
        if (rrid != null) {
            viewModel.LoadData(rrid)
        }
        if (data != null) {
            rr_date = data.timestamp
            rr_status = data.request_status
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RequestInfo(rrid, rr_date, rr_status)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            data?.receive_repair?.repair_details?.sortedByDescending { it.timestamp }?.let { repairDetails ->
                items(repairDetails.size) { index ->
                    val detail = repairDetails[index]
                    val isCurrent = index == 0 // ตรวจสอบว่าเป็นข้อมูลล่าสุดหรือไม่
                    val title = detail.rd_description!!.split(":")[0]
                    val description = detail.rd_description!!.split(":")[1]
                    StatusItem(title, description, formatTimestamp(detail.timestamp ?: "null"), isCurrent)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestInfo(rrid: String?, rr_date: String, rr_status: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E88E5))
            .padding(16.dp)
    ) {
        Text("รหัสแจ้งซ่อม ${rrid}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("ส่งคำขอเมื่อวันที่", color = Color.White)
        Text("${formatTimestamp(rr_date)}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("สถานะ : ${rr_status}", color = Color.White)
    }
}

@Composable
fun StatusItem(title: String, description: String, time: String, isCurrent: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isCurrent) Color.Blue else Color.Gray)
            )

        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                time,
                fontSize = 12.sp,
                color = if (isCurrent) Color.Blue else Color.Gray
            )
            Text(
                title,
                fontWeight = FontWeight.Bold,
                color = if (isCurrent) Color.Black else Color.Gray
            )
            Text(
                description,
                fontSize = 12.sp,
                color = if (isCurrent) Color.Blue else Color.Gray
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun StatusScreenPreview() {
    StatusScreen(rrid = "1")
}

data class Status(val time: String, val title: String, val description: String, val isCurrent: Boolean)
