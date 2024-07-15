package com.example.repaircomputerapplication_finalproject.screens.report

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportScreen() {
    var totalReports by remember { mutableStateOf(2) }
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            backgroundColor = Color(0xFF00E676),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("จำนวนการแจ้งซ่อมทั้งหมด", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$totalReports", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DatePickerButton("Start", startDate, onDateSelected = { selectedDate -> startDate = selectedDate })
            DatePickerButton("End", endDate, onDateSelected = { selectedDate -> endDate = selectedDate })
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* TODO: Implement work order generation */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2979FF)),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("ออกรายงาน", fontSize = 18.sp, color = Color.White)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerButton(label: String, date: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                // TODO: Implement date picker dialog
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7E57C2))
        ) {
            Text(date.format(dateFormatter), fontSize = 16.sp, color = Color.White)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ReportScreenPreview() {
    ReportScreen()
}