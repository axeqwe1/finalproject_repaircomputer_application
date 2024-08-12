import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.repaircomputerapplication_finalproject.`api-service`.ConnectionChecker
import com.example.repaircomputerapplication_finalproject.component.LoadSuccessScreen
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.LoadingScreen
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import com.example.repaircomputerapplication_finalproject.viewModel.RequestForRepairViewModel.formRequestViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun formRequestForRepair(navController: NavController, formRequestViewModel: formRequestViewModel = viewModel()) {
    val vmodel = formRequestViewModel
    val context = LocalContext.current
    var showLoadSuccess by remember { mutableStateOf(false) }
    var deviceId by remember { mutableStateOf("") }
    var empId: Int? by remember { mutableStateOf(null) }
    var repairDetails by remember { mutableStateOf("") }
    // ตัวแปรสำหรับ DropdownMenu
    val equipmentList = vmodel.equipmentResult.collectAsState().value
    val buildingList = vmodel.buildingResult.collectAsState().value
    val employeeList = vmodel.employeeList.collectAsState().value
    val imageName1 = vmodel.uploadedFileName1.collectAsState().value
    val imageName2 = vmodel.uploadedFileName2.collectAsState().value
    val imageName3 = vmodel.uploadedFileName3.collectAsState().value
    var selectedBuilding by remember { mutableStateOf("กรุณาเลือกข้อมูล") }
    var buildingId by remember { mutableStateOf(0) }
    var isExpand by remember { mutableStateOf(false) }
    val imageUri1 = vmodel.image1.collectAsState().value
    val imageUri2 = vmodel.image2.collectAsState().value
    val imageUri3 = vmodel.image3.collectAsState().value
    var showDialog by remember { mutableStateOf(false) }
    var role by remember { mutableStateOf("") }
    var emp_Fnm by remember { mutableStateOf("") }
    var emp_Lnm by remember { mutableStateOf("") }
    var eq_id: Int? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (ConnectionChecker.checkConnection()) {
            isLoading = false
        }
    }
    LaunchedEffect(key1 = true) {
        val roleData = context.dataStore.data.map { items ->
            items[stringPreferencesKey("role")]
        }.first()
        role = roleData ?: ""

        val EmpId = context.dataStore.data.map { items ->
            items[stringPreferencesKey("userId")]
        }.first()
        empId = EmpId?.toInt()
        Log.d(TAG, "formRequestForRepair: $role")
    }

    fun checkEmployeeData(firstName: String, lastName: String): Boolean {
        var isFound = false
        if (firstName != "" && lastName != "") {
            employeeList?.forEach { items ->
                Log.d(TAG, "checkEmployeeData: ${items.firstname} and $firstName")
                if (items.firstname == firstName && items.lastname == lastName) {
                    isFound = true
                    empId = items.emp_id
                }
            }
        }
        return isFound
    }

    fun checkEquipmentId(deviceId: String): Boolean {
        var isFound = false
        deviceId.toIntOrNull()?.let { id ->
            equipmentList?.forEach { items ->
                if (items.eq_id == id) {
                    isFound = true
                    eq_id = items.eq_id
                }
            }
        }
        return isFound
    }

    fun checkBuildingId(buildingId: Int): Boolean {
        var isFound = false
        buildingList?.forEach { items ->
            if (items.building_id == buildingId) {
                isFound = true
            }
        }
        return isFound
    }

    // ... (ตัวแปรสำหรับเมนูดรอปดาวน์และรูปภาพ)
    if (showLoadSuccess) {
        val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
        val scope = rememberCoroutineScope()
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                LoadSuccessScreen(onContinueClicked = { /* Handle continue action */ })
            },
        ) {}
        LaunchedEffect(key1 = true) {
            delay(5000)
            navController.navigate(ScreenRoutes.MenuNav.route) {
                popUpTo(ScreenRoutes.FormRequestForRepair.route)
            }
            sheetState.hide()
        }
    } else {
        if (isLoading) {
            LoadingScreen()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (role == "Technician") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = emp_Fnm,
                            onValueChange = { emp_Fnm = it },
                            label = { Text("ชื่อผู้แจ้ง") },
                            modifier = Modifier
                                .weight(1f)

                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        OutlinedTextField(
                            value = emp_Lnm,
                            onValueChange = { emp_Lnm = it },
                            label = { Text("นามสกุลผู้แจ้ง") },
                            modifier = Modifier
                                .weight(1f)

                        )
                    }
                }
                OutlinedTextField(
                    value = deviceId,
                    onValueChange = { deviceId = it },
                    label = { Text("รหัสอุปกรณ์") }
                )
                // TextArea สำหรับรายละเอียด
                OutlinedTextField(
                    value = repairDetails,
                    onValueChange = { repairDetails = it },
                    label = { Text("รายละเอียดการซ่อม") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp) // ปรับขนาดตามต้องการ
                )
                // dropdown
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    ExposedDropdownMenuBox(expanded = isExpand, onExpandedChange = { isExpand = it }) {
                        OutlinedTextField(
                            value = selectedBuilding,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("เลือกตึก/อาคาร") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpand)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isExpand,
                            onDismissRequest = {
                                isExpand = false
                            }
                        ) {
                            buildingList?.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = "ชื่อตึก:${item.building_name} เลขห้อง:${item.building_room_number} ชั้นตึก:${item.building_floor}") },
                                    onClick = {
                                        buildingId = item.building_id ?: 0
                                        selectedBuilding = "ชื่อตึก:${item.building_name} เลขห้อง:${item.building_room_number} ชั้นตึก:${item.building_floor}"
                                        isExpand = false
                                    }
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ImageUploadScreen(formRequestViewModel)
                    }
                }
                Button(
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Text("Submit")
                }
            }
        }
    }

    if (showDialog) {
        var alertMessage: String by remember { mutableStateOf("") }
        var titleMessage: String by remember { mutableStateOf("") }
        if (role == "Technician") {
            if (deviceId == "" ||
                selectedBuilding == "กรุณาเลือกข้อมูล" ||
                repairDetails == ""
            ) {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "กรุณากรอกข้อมูลให้ครบ"
            } else if (!checkEmployeeData(emp_Fnm, emp_Lnm)) {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "ไม่พบข้อมูลผู้ใช้งาน"
            } else if (!checkEquipmentId(deviceId)) {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "ไม่พบข้อมูลอุปกรณ์"
            } else if (!checkBuildingId(buildingId)) {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "ไม่พบข้อมูลตึกอาคาร"
            } else if (imageUri1?.path == null || imageUri2?.path == null || imageUri3?.path == null) {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "กรุณาใส่รูปภาพให้ครบ"
            } else {
                titleMessage = "แจ้งเตือน"
                alertMessage = "กรุณาตรวจสอบข้อมูลการแจ้งซ่อมให้ครบถ้วน คุณต้องการแจ้งซ่อมใช่หรือไม่?"
            }
        }else{
            if (deviceId == "" ||
                selectedBuilding == "กรุณาเลือกข้อมูล" ||
                repairDetails == ""
            ) {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "กรุณากรอกข้อมูลให้ครบ"
            }  else if (!checkEquipmentId(deviceId)) {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "ไม่พบข้อมูลอุปกรณ์"
            } else if (!checkBuildingId(buildingId)) {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "ไม่พบข้อมูลตึกอาคาร"
            } else if (imageUri1?.path == null || imageUri2?.path == null || imageUri3?.path == null) {
                titleMessage = "เกิดข้อผิดพลาด"
                alertMessage = "กรุณาใส่รูปภาพให้ครบ"
            } else {
                titleMessage = "แจ้งเตือน"
                alertMessage = "กรุณาตรวจสอบข้อมูลการแจ้งซ่อมให้ครบถ้วน คุณต้องการแจ้งซ่อมใช่หรือไม่?"
            }
        }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(titleMessage) },
            text = { Text(alertMessage) },
            confirmButton = {
                Button(onClick = {
                    if(titleMessage == "แจ้งเตือน"){
                        if (role == "Technician") {
                            if (checkEmployeeData(emp_Fnm, emp_Lnm) &&
                                checkEquipmentId(deviceId) &&
                                checkBuildingId(buildingId) &&
                                repairDetails.isNotBlank() &&
                                imageUri1?.path?.isNotEmpty() == true &&
                                imageUri2?.path?.isNotEmpty() == true &&
                                imageUri3?.path?.isNotEmpty() == true
                            ) {
                                showDialog = false  // ปิดใช้งาน Dialog หากข้อมูลครบ
                                vmodel.uploadImage(imageUri1, 1)
                                vmodel.uploadImage(imageUri2, 2)
                                vmodel.uploadImage(imageUri3, 3)
                                Log.d(TAG, "formRequestForRepair: $imageName1, $imageName2, $imageName3")
                                vmodel.sendRequest(repairDetails, empId ?: 0, buildingId, eq_id ?: 0)
                                showLoadSuccess = true
                            } else {
                                // โค้ดสำหรับการดำเนินการถัดไปหากข้อมูลครบถ้วน
                                showDialog = true
                                Log.d(TAG, "formRequestForRepair: ${imageUri1?.path}, ${imageUri2?.path}, ${imageUri3?.path}")
                            }
                        } else {
                            if (checkEquipmentId(deviceId) &&
                                checkBuildingId(buildingId) &&
                                repairDetails.isNotBlank() &&
                                imageUri1?.path?.isNotEmpty() == true &&
                                imageUri2?.path?.isNotEmpty() == true &&
                                imageUri3?.path?.isNotEmpty() == true) {
                                showDialog = false  // เปิดใช้งาน Dialog หากข้อมูลไม่ครบ
                                vmodel.uploadImage(imageUri1!!, 1)
                                vmodel.uploadImage(imageUri2!!, 2)
                                vmodel.uploadImage(imageUri3!!, 3)
                                vmodel.sendRequest(repairDetails, empId ?: 0, buildingId, eq_id ?: 0)
                                Log.d(TAG, "formRequestForRepair: $empId")
                                showLoadSuccess = true
                            } else {
                                // โค้ดสำหรับการดำเนินการถัดไปหากข้อมูลครบถ้วน
                                showDialog = true
                            }
                        }
                        showDialog = false
                    }else{
                        showDialog = false
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                if(titleMessage == "แจ้งเตือน"){
                    Button(onClick = { showDialog = false }) {
                        Text(text = "Cancel")
                    }
                }
            }
        )
    }
}