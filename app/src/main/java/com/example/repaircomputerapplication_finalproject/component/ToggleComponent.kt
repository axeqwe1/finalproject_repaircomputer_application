package com.example.repaircomputerapplication_finalproject.component

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ToggleComponent(role: String, onModeChange: (String) -> Unit) {
    var selected by remember { mutableStateOf("") }

    // Initialize selected based on role
    LaunchedEffect(role) {
        selected = when (role) {
            "Admin" -> "งานที่ยังไม่จ่าย"
            "Technician" -> "งานทั้งหมด"
            else -> ""
        }
    }

    // Define the texts for the toggle buttons based on role
    val firstOption = if (role == "Admin") "งานที่ยังไม่จ่าย" else "งานทั้งหมด"
    val secondOption = if (role == "Admin") "งานทั้งหมด" else "ไม่ได้กรอกข้อมูล"

    Row(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFFD0E6F8), RoundedCornerShape(50.dp))
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp))
                .background(if (selected == firstOption) Color.Yellow else Color(0xFFCCCCCC))
                .clickable {
                    selected = firstOption
                    onModeChange(selected)
                }
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = firstOption,
                color = if (selected == firstOption) Color.Black else Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(40.dp)
                .background(Color.Black)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp))
                .background(if (selected == secondOption) Color.Yellow else Color(0xFFCCCCCC))
                .clickable {
                    selected = secondOption
                    onModeChange(selected)
                }
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = secondOption,
                color = if (selected == secondOption) Color.Black else Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
