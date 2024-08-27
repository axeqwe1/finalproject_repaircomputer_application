import android.widget.Toast
import androidx.compose.foundation.background
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
    userType: String?,
    isEdit: Boolean?,
    userId: String?,
    viewModel: UserManageViewModel = viewModel()
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
    var submitBtnName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var titleMessage by remember { mutableStateOf("") }
    var confirmationMessage by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val adminList = viewModel.admin.collectAsState().value ?: emptyList()
    val techList = viewModel.tech.collectAsState().value ?: emptyList()
    val empList = viewModel.emp.collectAsState().value ?: emptyList()
    val chiefList = viewModel.chief.collectAsState().value ?: emptyList()
    val departList = viewModel.department.collectAsState().value ?: emptyList()
    val techStatusList = viewModel.techStatus.collectAsState().value ?: emptyList()
    var statusId by remember { mutableStateOf("") }
    var departmentId by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        if (userType != null) {
            viewModel.loadData(userType)
        }
    }

    LaunchedEffect(adminList, techList, empList, chiefList, departList, techStatusList) {
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
                        department = user.departmentId?.let { viewModel.getDepartmentName(it) }.toString()
                        departmentId = user.departmentId?.toString() ?: ""
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
                        departmentId = user.departmentId?.toString() ?: ""
                        department = user.departmentId?.let { viewModel.getDepartmentName(it) }.toString()
                        statusId = user.status_id?.toString() ?: ""
                        techStatusList.forEach { item ->
                            if(statusId == item.status_id.toString()){
                                status = item.receive_request_status.toString()
                            }
                        }
                    }
                }
                "Employee" -> {
                    val user = empList.find { it.emp_id.toString() == userId }
                    if (user != null) {
                        firstName = user.firstname ?: ""
                        lastName = user.lastname ?: ""
                        email = user.email ?: ""
                        password = user.password ?: ""
                        phone = user.phone ?: ""
                        department = user.departmentId?.let { viewModel.getDepartmentName(it) }.toString()
                        departmentId = user.departmentId?.toString() ?: ""
                    }
                }
                "Chief" -> {
                    val user = chiefList.find { it.chief_id.toString() == userId }
                    if (user != null) {
                        firstName = user.firstname ?: ""
                        lastName = user.lastname ?: ""
                        email = user.email ?: ""
                        password = user.password ?: ""
                        phone = user.phone ?: ""
                        department = user.departmentId?.let { viewModel.getDepartmentName(it) }.toString()
                        departmentId = user.departmentId?.toString() ?: ""
                    }
                }
            }
        }
    }

    val scrollState = rememberScrollState()

    fun validateForm(): Boolean {
        when {
            firstName.isBlank() -> {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "กรุณากรอกชื่อจริง"
                showDialog = true
                return false
            }
            lastName.isBlank() -> {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "กรุณากรอกนามสกุล"
                showDialog = true
                return false
            }
            email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "กรุณากรอกอีเมลที่ถูกต้อง"
                showDialog = true
                return false
            }
            password.isBlank() -> {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "กรุณากรอกรหัสผ่าน"
                showDialog = true
                return false
            }
            password != confirmPassword -> {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "รหัสผ่านและการยืนยันรหัสผ่านไม่ตรงกัน"
                showDialog = true
                return false
            }
            phone.isBlank() || !phone.matches(Regex("\\d{10}")) -> {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "กรุณากรอกหมายเลขโทรศัพท์ที่ถูกต้อง (10 หลัก)"
                showDialog = true
                return false
            }
            department.isBlank() -> {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "กรุณาเลือกหน่วยงาน"
                showDialog = true
                return false
            }
            userType == "Technician" && status.isBlank() && isEdit == true -> {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "กรุณาเลือกสถานะ"
                showDialog = true
                return false
            }
            departmentId.isBlank() -> {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "ID ของหน่วยงานไม่ถูกต้อง"
                showDialog = true
                return false
            }
            else -> return true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA))
            .padding(24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isEdit == false) {
            Text("เพิ่มข้อมูล", fontSize = 24.sp, color = Color.Black)
            submitBtnName = "เพิ่มข้อมูล"
        } else {
            Text("แก้ไขข้อมูล", fontSize = 24.sp, color = Color.Black)
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
                departList.forEach { item ->
                    DropdownMenuItem(onClick = {
                        department = item.departmentName
                        departmentId = item.department_id.toString()
                        expandedDepartment = false
                    }) {
                        Text(item.departmentName)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for Status
        if (userType == "Technician" && isEdit == true) {
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
                    techStatusList.forEach { item ->
                        DropdownMenuItem(onClick = {
                            status = item.receive_request_status.toString()
                            statusId = item.status_id.toString()
                            expandedStatus = false
                        }) {
                            Text(item.receive_request_status.toString())
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (validateForm()) {
                    confirmationMessage = if (isEdit == true) {
                        "คุณต้องการแก้ไขข้อมูลหรือไม่?"
                    } else {
                        "คุณต้องการเพิ่มข้อมูลหรือไม่?"
                    }
                    showConfirmationDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(submitBtnName, fontSize = 18.sp)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(titleMessage) },
                text = { Text(alertMessage) },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = { Text("ยืนยันการทำงาน") },
                text = { Text(confirmationMessage) },
                confirmButton = {
                    Button(onClick = {
                        showConfirmationDialog = false
                        if (isEdit == true) {
                            if (departmentId.isNotBlank() && departmentId.toIntOrNull() != null) {
                                if (userType == "Technician") {
                                    viewModel.editUser(
                                        userType ?: "null",
                                        userId ?: "null",
                                        firstName,
                                        lastName,
                                        email,
                                        password,
                                        phone,
                                        departmentId,
                                        statusId
                                    )
                                } else {
                                    viewModel.editUser(
                                        userType ?: "null",
                                        userId ?: "null",
                                        firstName,
                                        lastName,
                                        email,
                                        password,
                                        phone,
                                        departmentId,
                                        "0"
                                    )
                                }
                            } else {
                                titleMessage = "เกิดข้อผิดพลาด"
                                alertMessage = "ID ของหน่วยงานไม่ถูกต้อง"
                                showDialog = true
                            }
                        } else {

                            if (userType == "Technician") {
                                techStatusList.forEach {
                                        item ->
                                    if(item.receive_request_status == "A"){
                                        statusId = item.status_id.toString()
                                    }
                                }
                                viewModel.addUser(
                                    userType ?: "null",
                                    firstName,
                                    lastName,
                                    email,
                                    password,
                                    phone,
                                    departmentId,
                                    statusId
                                )
                            } else {
                                viewModel.addUser(
                                    userType ?: "null",
                                    firstName,
                                    lastName,
                                    email,
                                    password,
                                    phone,
                                    departmentId,
                                    "null"
                                )
                            }
                        }
                        showSuccessDialog = true
                    }) {
                        Text("ยืนยัน")
                    }
                },
                dismissButton = {
                    Button(onClick = { showConfirmationDialog = false }) {
                        Text("ยกเลิก")
                    }
                }
            )
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("สำเร็จ") },
                text = { Text(if (isEdit == true) "แก้ไขข้อมูลสำเร็จ" else "เพิ่มข้อมูลสำเร็จ") },
                confirmButton = {
                    Button(onClick = { showSuccessDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
