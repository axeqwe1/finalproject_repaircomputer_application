package com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.managementUser
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.model.AdminData
import com.example.repaircomputerapplication_finalproject.model.ChiefData
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.TechnicianData
import com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel.UserManageViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun crudUserScreen(userType:String?,navController: NavController,viewModel:UserManageViewModel = viewModel()){
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val adminList = viewModel.admin.collectAsState().value ?: emptyList()
    val techList = viewModel.tech.collectAsState().value ?: emptyList()
    val empList = viewModel.emp.collectAsState().value ?: emptyList()
    val chiefList = viewModel.chief.collectAsState().value ?: emptyList()
    val departList = viewModel.department.collectAsState().value ?: emptyList()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(adminList, techList, empList, chiefList, searchQuery) {
        viewModel.loadData()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(ScreenRoutes.AddUserForm.passUserTypeAndIsEditAndUserId(UserType = userType!!, isEdit = false)) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
    ) { innerPading ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0F7FA))
                .padding(innerPading),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("จัดการข้อมูล : $userType", fontSize = 24.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("ค้นหา") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            when (userType) {
                "Admin" -> UserList(adminList.filterBySearchQuery(searchQuery.text, listOf(
                    { it.email },
                    { it.firstname + " " + it.lastname},
                    { it.admin_id.toString() },
                    { departList.find { department -> department.department_id == it.departmentId }?.departmentName ?: "Unknown" }
                ))) { AdminCard(it,userType,navController) }
                "Technician" -> UserList(techList.filterBySearchQuery(searchQuery.text, listOf(
                    { it.email },
                    { it.firstname + " " + it.lastname},
                    { it.tech_id.toString() },
                    { departList.find { department -> department.department_id == it.departmentId }?.departmentName ?: "Unknown" }
                ))) { TechnicianCard(it,userType,navController) }
                "Employee" -> UserList(empList.filterBySearchQuery(searchQuery.text, listOf(
                    { it.email },
                    { it.firstname + " " + it.lastname},
                    { it.emp_id.toString() },
                    { departList.find { department -> department.department_id == it.departmentId }?.departmentName ?: "Unknown" }
                ))) { EmployeeCard(it,userType,navController) }
                "Chief" -> UserList(chiefList.filterBySearchQuery(searchQuery.text, listOf(
                    { it.email },
                    { it.firstname + " " + it.lastname},
                    { it.chief_id.toString() },
                    { departList.find { department -> department.department_id == it.departmentId }?.departmentName ?: "Unknown" }
                ))) { ChiefCard(it,userType,navController) }
                else -> Text("Invalid user type", color = Color.Red, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

fun <T> List<T>.filterBySearchQuery(searchQuery: String, selectors: List<(T) -> String?>): List<T> {
    return this.filter { item ->
        selectors.any { selector ->
            selector(item)?.contains(searchQuery, ignoreCase = true) ?: false
        }
    }
}

@Composable
fun <T> UserList (userList:List<T>, itemContent:@Composable (T) -> Unit){
    LazyColumn{
        items(userList){
            user -> itemContent(user)
        }
    }
}

@Composable
fun AdminCard(adminData: AdminData,userType: String?,navController: NavController) {
    UserCard(
        id = adminData.admin_id,
        firstname = adminData.firstname,
        lastname = adminData.lastname,
        email = adminData.email,
        phone = adminData.phone,
        departmentId = adminData.departmentId,
        userType = userType,
        navController = navController,
        status = null
    )
}

@Composable
fun EmployeeCard(employeeData: EmployeeData,userType: String?,navController: NavController) {
    UserCard(
        id = employeeData.emp_id,
        firstname = employeeData.firstname,
        lastname = employeeData.lastname,
        email = employeeData.email,
        phone = employeeData.phone,
        departmentId = employeeData.departmentId,
        userType = userType,
        navController = navController,
        status = null
    )
}

@Composable
fun TechnicianCard(technicianData: TechnicianData,userType: String?,navController: NavController) {
    UserCard(
        id = technicianData.tech_id,
        firstname = technicianData.firstname,
        lastname = technicianData.lastname,
        email = technicianData.email,
        phone = technicianData.phone,
        departmentId = technicianData.departmentId,
        userType = userType,
        navController = navController,
        status = technicianData.status_id.toString()
    )
}

@Composable
fun ChiefCard(chiefData: ChiefData,userType: String?,navController: NavController) {
    UserCard(
        id = chiefData.chief_id,
        firstname = chiefData.firstname,
        lastname = chiefData.lastname,
        email = chiefData.email,
        phone = chiefData.phone,
        departmentId = chiefData.departmentId,
        userType = userType,
        navController = navController,
        status = null
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserCard(
    id: Int?,
    firstname: String?,
    lastname: String?,
    email: String?,
    phone: String?,
    departmentId: Int?,
    status:String?,
    navController: NavController,
    userType: String?,
    viewModel: UserManageViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var departName by remember { mutableStateOf("") }
    var statusName by remember { mutableStateOf("Loading...") }

    LaunchedEffect(status,departmentId){
        coroutineScope {
            statusName = viewModel.getTechStatusName(status?.toInt() ?: 0)
            departName = viewModel.getDepartmentName(departmentId ?: 0)
        }
    }
    ConfirmDeleteDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            if(userType != null && id != null) {
                viewModel.deleteUser(userType = userType, userId = id)
                showDialog = false
                viewModel.loadData()
            }
        }
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    navController
                        .navigate(
                            ScreenRoutes.AddUserForm
                                .passUserTypeAndIsEditAndUserId(
                                    UserType = userType!!,
                                    isEdit = true,
                                    UserId = id.toString()
                                )
                        )
                },
                onLongClick = {
                    showDialog = true
                }
            )
                   ,
        backgroundColor = Color.LightGray,
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text("รหัส: $id", fontSize = 16.sp)
            Text("ชื่อ: $firstname $lastname", fontSize = 16.sp)
            Text("Email: $email", fontSize = 16.sp)
            Text("Phone: $phone", fontSize = 16.sp)
            Text("หน่วยงาน: $departName", fontSize = 16.sp)
            if(userType == "Technician"){
                Text("สถานะ: $statusName", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete this item?") },
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
