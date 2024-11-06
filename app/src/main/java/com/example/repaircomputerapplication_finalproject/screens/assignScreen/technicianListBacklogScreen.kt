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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.model.TechBacklogCount
import com.example.repaircomputerapplication_finalproject.viewModel.AssignWork.techListBacklogViewModel
import kotlinx.coroutines.launch

@Composable
fun technicianListBacklogScreen(navController: NavController, admin_id: String, rrid: String, viewModel: techListBacklogViewModel = viewModel()) {
    val techList = viewModel.techList.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC)) // Light blue background
            .padding(16.dp)
    ) {
        techList?.let { list ->
            // Sort the list to show 'Available' status first, then by 'tech_id' in ascending order
            val sortedList = list.sortedWith(
                compareBy({ it.status_id != 2 }, { it.tech_id })
            )

            items(sortedList) { item ->
                var backlog by remember { mutableStateOf(TechBacklogCount()) }
                LaunchedEffect(item.tech_id) {
                    if (item.tech_id != null) {
                        backlog = viewModel.fetchTechBacklog(item.tech_id)
                    }
                }
                ItemCard(
                    tech_id = item.tech_id.toString(),
                    techName = "${item.firstname} ${item.lastname}",
                    status = when (item.status_id) {
                        1 -> "Unavailable"
                        2 -> "Available"
                        else -> "Unknown"
                    },
                    received = backlog.TotalWork.toString(),
                    pending = backlog.Backlog.toString(),
                    completed = backlog.SuccessWork.toString(),
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
    rrid: String,
    navController: NavController,
    viewModel: techListBacklogViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    ConfirmAssignWork(
        showDialog = showDialog,
        techName = techName,
        onDismiss = { showDialog = false },
        onConfirm = {
            viewModel.assignWork(
                admin_id = admin_id.toInt(),
                tech_id = tech_id.toInt(),
                rrid = rrid.toInt(),
            )
            showDialog = false
            navController.navigateUp()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
            .background(Color(0xFFEEEEEE), RoundedCornerShape(10.dp)) // Light grey background
            .padding(16.dp)
    ) {
        Text(text = tech_id, fontWeight = FontWeight.Bold)
        Text(text = techName)
        Text(text = "Status: $status")
        Text(text = "Total Work: $received")
        Text(text = "Pending: $pending")
        Text(text = "Completed: $completed")
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
