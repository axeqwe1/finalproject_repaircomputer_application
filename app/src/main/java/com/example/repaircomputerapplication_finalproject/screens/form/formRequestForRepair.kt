import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun formRequestForRepair(navController: NavController, formRequestViewModel: formRequestViewModel = viewModel()) {
    val vmodel = formRequestViewModel
    val context = LocalContext.current
    var showLoadSuccess by remember { mutableStateOf(false) }
    var deviceId by remember { mutableStateOf("") }
    var empId: Int? by remember { mutableStateOf(null) }
    var repairDetails by remember { mutableStateOf("") }

    // Dropdown menu variables
    val equipmentList = vmodel.equipmentResult.collectAsState().value
    val buildingList = vmodel.buildingResult.collectAsState().value
    val employeeList = vmodel.employeeList.collectAsState().value
    var selectedBuilding by remember { mutableStateOf("กรุณาเลือกข้อมูล") }
    var selectedFloor by remember { mutableStateOf("กรุณาเลือกชั้น") }
    var selectedRoom by remember { mutableStateOf("กรุณาเลือกห้อง") }
    var isBuildingExpand by remember { mutableStateOf(false) }
    var isFloorExpand by remember { mutableStateOf(false) }
    var isRoomExpand by remember { mutableStateOf(false) }
    var buildingId by remember { mutableStateOf(0) }
    val imageUri1 = vmodel.image1.collectAsState().value
    val imageUri2 = vmodel.image2.collectAsState().value
    val imageUri3 = vmodel.image3.collectAsState().value
    var showDialog by remember { mutableStateOf(false) }
    var role by remember { mutableStateOf("") }
    var emp_Fnm by remember { mutableStateOf("") }
    var emp_Lnm by remember { mutableStateOf("") }
    var eq_id: Int? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(true) }

    val showErrorDialog = vmodel.showErrorDialog.collectAsState().value
    val errorMessage = vmodel.errorMessage.collectAsState().value

    val coroutineScope = rememberCoroutineScope()
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
        if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
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
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = emp_Fnm,
                            onValueChange = { emp_Fnm = it },
                            label = { Text("ชื่อผู้แจ้ง") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        OutlinedTextField(
                            value = emp_Lnm,
                            onValueChange = { emp_Lnm = it },
                            label = { Text("นามสกุลผู้แจ้ง") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                OutlinedTextField(
                    value = deviceId,
                    onValueChange = { deviceId = it },
                    label = { Text("รหัสอุปกรณ์") }
                )
                OutlinedTextField(
                    value = repairDetails,
                    onValueChange = { repairDetails = it },
                    label = { Text("รายละเอียดการซ่อม") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Dropdown สำหรับเลือกชื่อตึก/อาคาร
                    ExposedDropdownMenuBox(
                        expanded = isBuildingExpand,
                        onExpandedChange = { isBuildingExpand = it }
                    ) {
                        OutlinedTextField(
                            value = selectedBuilding,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("เลือกตึก/อาคาร") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isBuildingExpand)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isBuildingExpand,
                            onDismissRequest = { isBuildingExpand = false }
                        ) {
                            buildingList?.distinctBy { it.building_name }?.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item.building_name) },
                                    onClick = {
                                        buildingId = item.building_id ?: 0
                                        selectedBuilding = item.building_name
                                        selectedFloor = "กรุณาเลือกชั้น"
                                        selectedRoom = "กรุณาเลือกห้อง"
                                        isBuildingExpand = false
                                    }
                                )
                            }
                        }
                    }

                    if (selectedBuilding != "กรุณาเลือกข้อมูล") {
                        ExposedDropdownMenuBox(
                            expanded = isFloorExpand,
                            onExpandedChange = { isFloorExpand = it }
                        ) {
                            OutlinedTextField(
                                value = selectedFloor,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("เลือกชั้นตึก") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isFloorExpand)
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = isFloorExpand,
                                onDismissRequest = { isFloorExpand = false }
                            ) {
                                buildingList?.filter { it.building_name == selectedBuilding }
                                    ?.distinctBy { it.building_floor }
                                    ?.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(text = item.building_floor.toString()) },
                                            onClick = {
                                                selectedFloor = item.building_floor.toString()
                                                selectedRoom = "กรุณาเลือกห้อง"
                                                isFloorExpand = false
                                            }
                                        )
                                    }
                            }
                        }
                    }

                    if (selectedFloor != "กรุณาเลือกชั้น") {
                        ExposedDropdownMenuBox(
                            expanded = isRoomExpand,
                            onExpandedChange = { isRoomExpand = it }
                        ) {
                            OutlinedTextField(
                                value = selectedRoom,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("เลือกเลขห้อง") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRoomExpand)
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = isRoomExpand,
                                onDismissRequest = { isRoomExpand = false }
                            ) {
                                buildingList?.filter { it.building_name == selectedBuilding && it.building_floor.toString() == selectedFloor }
                                    ?.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(text = item.building_room_number) },
                                            onClick = {
                                                selectedRoom = item.building_room_number
                                                isRoomExpand = false
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
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = { showDialog = true }
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
                if (deviceId == "" || selectedBuilding == "กรุณาเลือกข้อมูล" || repairDetails == "") {
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
            } else {
                if (deviceId == "" || selectedBuilding == "กรุณาเลือกข้อมูล" || repairDetails == "") {
                    titleMessage = "เกิดข้อผิดพลาด"
                    alertMessage = "กรุณากรอกข้อมูลให้ครบ"
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
            }

            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(titleMessage) },
                text = { Text(alertMessage) },
                confirmButton = {
                    Button(onClick = {
                        coroutineScope.launch {
                            if (titleMessage == "แจ้งเตือน") {
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
                                        val isSuccess = performRepairRequest(
                                            vmodel,
                                            repairDetails,
                                            empId ?: 0,
                                            buildingId,
                                            eq_id ?: 0,
                                            listOf(imageUri1, imageUri2, imageUri3)
                                        )
                                        if (isSuccess) {
                                            showLoadSuccess = true
                                        }
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
                                        imageUri3?.path?.isNotEmpty() == true
                                    ) {
                                        showDialog = false  // เปิดใช้งาน Dialog หากข้อมูลไม่ครบ
                                        val isSuccess = performRepairRequest(
                                            vmodel,
                                            repairDetails,
                                            empId ?: 0,
                                            buildingId,
                                            eq_id ?: 0,
                                            listOf(imageUri1, imageUri2, imageUri3)
                                        )
                                        if (isSuccess) {
                                            showLoadSuccess = true
                                        }
                                        Log.d(TAG, "formRequestForRepair: $empId")
                                    } else {
                                        // โค้ดสำหรับการดำเนินการถัดไปหากข้อมูลครบถ้วน
                                        showDialog = true
                                    }
                                }
                                showDialog = false
                            } else {
                                showDialog = false
                            }
                        }
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    if (titleMessage == "แจ้งเตือน") {
                        Button(onClick = { showDialog = false }) {
                            Text(text = "Cancel")
                        }
                    }
                }
            )
        }
    if (showLoadSuccess && !showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showLoadSuccess = false },
            title = { Text("แจ้งซ่อมสำเร็จ") },
            text =  { Text("แจ้งซ่อมสำเร็จ") } ,
            confirmButton = {
                Button(onClick = {
                    showLoadSuccess = false
                    navController.navigateUp()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {

            }
        )
    }
    if (showErrorDialog) {
        showLoadSuccess = false
        AlertDialog(
            onDismissRequest = { formRequestViewModel.resetErrorDialog() },
            title = { Text("เกิดข้อผิดพลาด") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { formRequestViewModel.resetErrorDialog() }) {
                    Text("ตกลง")
                }
            }
        )
    }
}
suspend fun performRepairRequest(
    vmodel: formRequestViewModel,
    repairDetails: String,
    empId: Int,
    buildingId: Int,
    eq_id: Int,
    imageUris: List<Uri>
): Boolean = coroutineScope {
    try {
        // ใช้ async สำหรับการทำงานแบบคู่ขนาน
        val sendRequest = async { vmodel.sendRequest(repairDetails, empId, buildingId, eq_id) }

        // รอให้ sendRequest เสร็จสิ้นก่อน
        sendRequest.await()

        // ตรวจสอบว่าไม่มีข้อผิดพลาด
        if (!vmodel.showErrorDialog.value) {
            // ทำการอัปโหลดภาพ
            val uploadJobs = imageUris.mapIndexed { index, uri ->
                async { vmodel.uploadImage(uri, index + 1) }
            }

            // รอให้การอัปโหลดเสร็จสิ้นทั้งหมด
            uploadJobs.awaitAll()
            true
        } else {
            false
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error performing repair request: ${e.message}")
        false
    }
}