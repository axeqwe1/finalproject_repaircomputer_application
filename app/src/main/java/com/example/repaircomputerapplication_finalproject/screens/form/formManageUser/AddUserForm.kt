import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel.UserManageViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddUserForm(
    userType:String?,
    isEdit:Boolean?,
    userId:String?,
    viewModel:UserManageViewModel = viewModel()
) {
    val context = LocalContext.current
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }
    var expandedDepartment by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }

    var submitBtnName by remember{ mutableStateOf("") }

    val adminList = viewModel.admin.collectAsState().value ?: emptyList()
    val techList = viewModel.tech.collectAsState().value ?: emptyList()
    val empList = viewModel.emp.collectAsState().value ?: emptyList()
    val chiefList = viewModel.chief.collectAsState().value ?: emptyList()
    LaunchedEffect(userType, isEdit) {
        if (isEdit == true && userType != null && userId != null) {
            viewModel.loadData(userType)
        }
    }
    LaunchedEffect(adminList, techList, empList, chiefList) {
        if (isEdit == true && userId != null) {
            when (userType) {
                "Admin" -> {
                    val user = adminList.find { it.admin_id.toString() == userId }
                    if (user != null) {
                        firstName = user.firstname ?: ""
                        lastName = user.lastname ?: ""
                        email = user.email ?: ""
                        password = user.password ?: ""
                        phone = user.phone ?: ""
                        department = user.departmentId.toString()
                    }
                }
                "Technician" -> {
                    val user = techList.find { it.tech_id.toString() == userId }
                    if (user != null) {
                        firstName = user.firstname ?: ""
                        lastName = user.lastname ?: ""
                        email = user.email ?: ""
                        password = user.password ?: ""
                        phone = user.phone ?: ""
                        department = user.departmentId.toString()
                        status = user.status_id.toString()
                    }
                }
                // เพิ่ม logic สำหรับ Technician, Employee, Chief
            }
        }
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA))
            .padding(24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isEdit == false){
            Text("UserType is $userType isEdit is false and Id Is $userId", fontSize = 24.sp, color = Color.Black)
            submitBtnName = "เพิ่มข้อมูล"
        }else{
            Text("UserType is $userType isEdit is true and Id Is $userId", fontSize = 24.sp, color = Color.Black)
            submitBtnName = "แก้ไขข้อมูล"
        }


        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("ชื่อจริง") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("นามสกุล") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, "")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for Department
        ExposedDropdownMenuBox(
            expanded = expandedDepartment,
            onExpandedChange = { expandedDepartment = !expandedDepartment }
        ) {
            TextField(
                value = department,
                onValueChange = { department = it },
                label = { Text("หน่วยงาน") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDepartment)
                },
                readOnly = true
            )
            ExposedDropdownMenu(
                expanded = expandedDepartment,
                onDismissRequest = { expandedDepartment = false }
            ) {
                DropdownMenuItem(onClick = {
                    department = "1"
                    expandedDepartment = false
                }) {
                    Text("1")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Dropdown for Status
        if(userType == "Technician"){
            ExposedDropdownMenuBox(
                expanded = expandedStatus,
                onExpandedChange = { expandedStatus = !expandedStatus }
            ) {
                TextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text("สถานะ") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
                    },
                    readOnly = true
                )
                ExposedDropdownMenu(
                    expanded = expandedStatus,
                    onDismissRequest = { expandedStatus = false }
                ) {
                    DropdownMenuItem(onClick = {
                        status = "1"
                        expandedStatus = false
                    }) {
                        Text("1")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when(isEdit){
                    true -> {viewModel.editUser(userType ?: "null",userId ?: "null",firstName,lastName,email,password,phone,department,status)}
                    false -> {viewModel.addUser(userType ?: "null",firstName,lastName,email,password,phone,department)}
                    else -> { Toast.makeText(context,"Condition Wrong",Toast.LENGTH_SHORT).show()}
                }
              },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(submitBtnName, fontSize = 18.sp)
        }
    }
}
