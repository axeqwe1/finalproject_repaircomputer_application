package com.example.repaircomputerapplication_finalproject.screens.assignScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.viewModel.AssignWork.techListBacklogViewModel

@Composable
fun technicianListBacklogScreen(navController: NavController,admin_id:String,rrid:String,viewModel: techListBacklogViewModel = viewModel()) {
    val techList = viewModel.techList.collectAsState().value
    var id by remember{ mutableStateOf("") }
    var techName by remember{ mutableStateOf("") }
    var techStatus by remember{ mutableStateOf("") }
    var received by remember{ mutableStateOf("") }
    var pending by remember{ mutableStateOf("") }
    var successwork by remember{ mutableStateOf("") }
    LaunchedEffect(techList) {
        // Perform actions based on techList changes if needed
        techList?.forEach{
            item ->
            id = item.tech_id.toString()
            techName = viewModel.getTechName(item.tech_id ?: 0)
            if(viewModel.getTechnicianStatus(item.status_id ?: 0) == "UA"){
                techStatus = "Unavaliable"
            }else if(viewModel.getTechnicianStatus(item.status_id ?: 0) == "A"){
                techStatus = "Avaliable"
            }else{
                techStatus = "null"
            }
            received = viewModel.fetchTechBacklog(item.tech_id ?: 0).TotalWork.toString()
            pending = viewModel.fetchTechBacklog(item.tech_id ?: 0).Backlog.toString()
            successwork = viewModel.fetchTechBacklog(item.tech_id ?: 0).SuccessWork.toString()
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC)) // Light blue background
            .padding(16.dp)
    ) {
        if (techList != null) {
            items(techList.size) { item ->
                ItemCard(
                    tech_id = id,
                    techName = techName,
                    status = techStatus,
                    received = received,
                    pending = pending,
                    completed = successwork,
                    admin_id = admin_id,
                    rrid = rrid,
                    navController = navController,
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ItemCard(
    tech_id: String,
    techName: String,
    status: String,
    received: String,
    pending: String,
    completed: String,
    admin_id: String,
    rrid:String,
    navController: NavController,
    viewModel: techListBacklogViewModel) {
    var showDialog by remember{ mutableStateOf(false) }
    ConfirmAssignWork(
        showDialog = showDialog,
        techName = techName,
        onDismiss = {showDialog = false},
        onConfirm = {
            if(tech_id != null){
                viewModel.assignWork(
                    admin_id = admin_id.toInt(),
                    tech_id = tech_id.toInt(),
                    rrid = rrid.toInt(),
                )
                showDialog = false
                navController.navigateUp()
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDialog = true
            }
            .background(Color(0xFFEEEEEE), RoundedCornerShape(10.dp)) // Light grey background
            .padding(16.dp)
    ) {
        Text(text = tech_id, fontWeight = FontWeight.Bold)
        Text(text = techName)
        Text(text = "สถานะ : $status")
        Text(text = "จำนวนงานที่รับทั้งหมด : $received")
        Text(text = "จำนวนงานที่ค้าง : $pending")
        Text(text = "จำนวนงานที่เสร็จ : $completed")
    }
}

@Composable
fun ConfirmAssignWork(
    showDialog: Boolean,
    techName:String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
){
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Confirm AssignWork") },
            text = { Text(text = "Are you sure you want to AssignWork to $techName") },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemScreen() {
    val navController = rememberNavController()
}
