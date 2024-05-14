package com.example.repaircomputerapplication_finalproject.screens.manageDatascreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun manageAdminScreen(){
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val adminList = listOf(
        AdminData("001", "นายสมวง นอนกลิ้ง", "Admin@GG.com", "123-123-4444", "สำนักงาน"),
        // Add more admin data here
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Handle add action */ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
    ) { innerPading ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0F7FA))
                .padding(innerPading)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("ข้อมูลแอดมิน", fontSize = 24.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("ค้นหา") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            adminList.filter {
                it.name.contains(searchQuery.text, ignoreCase = true)
            }.forEach { admin ->
                AdminCard(admin)
            }
        }
    }
}

@Composable
fun AdminCard(admin: AdminData) {
    Card(
        backgroundColor = Color.LightGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("รหัส: ${admin.id}", fontSize = 16.sp)
            Text("ชื่อ: ${admin.name}", fontSize = 16.sp)
            Text("Email: ${admin.email}", fontSize = 16.sp)
            Text("Phone: ${admin.phone}", fontSize = 16.sp)
            Text("หน่วยงาน: ${admin.unit}", fontSize = 16.sp)
        }
    }
}

data class AdminData(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val unit: String
)