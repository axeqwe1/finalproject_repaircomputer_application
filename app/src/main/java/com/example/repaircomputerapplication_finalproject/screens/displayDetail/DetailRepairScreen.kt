
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.LoadingScreen
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import com.example.repaircomputerapplication_finalproject.viewModel.DisplayViewModel.RepairDetailViewModel
import com.example.repaircomputerapplication_finalproject.utils.formatTimestamp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailRepairScreen(
    rrid: String,
    userType: String,
    navController: NavController,
    viewModel: RepairDetailViewModel = viewModel()
) {
    val data by viewModel.detailData.collectAsState()
    val imagePainters by viewModel.imagePainters.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var _rrid by remember { mutableStateOf("") }
    var eq_id by remember { mutableStateOf("") }
    var eq_name by remember { mutableStateOf("") }
    var eqc_name by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var building by remember { mutableStateOf("") }
    var building_roomnum by remember { mutableStateOf("") }
    var building_floor by remember { mutableStateOf("") }
    var employeeName by remember { mutableStateOf("") }
    var rr_description by remember { mutableStateOf("") }
    var hasDetail by remember { mutableStateOf(false) }
    var technicianName by remember { mutableStateOf("") }
    var dateReceive by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("ยังไม่กรอกข้อมูล") }
    var levelOfDamage by remember { mutableStateOf("ยังไม่กรอกข้อมูล") }
    var admin_id by remember { mutableStateOf("") }
    var rd_id by remember { mutableStateOf("") }
    var requestStatus by remember { mutableStateOf("") }
    var detailTimeStamp by remember { mutableStateOf("") }
    var detailDescription by remember { mutableStateOf("") }

    // State for showing enlarged image dialog
    var showDialog by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<ByteArray?>(null) }

    LaunchedEffect(data) {
        if (userType == "Admin") {
            admin_id = context.dataStore.data.map { item ->
                item[stringPreferencesKey("userId")]
            }.first() ?: "null"
        } else {
            admin_id = "null"
        }
        viewModel.loadData(rrid)
        if (data != null && data!!.receive_repair == null && data!!.assign_work == null) {
            _rrid = data!!.rrid.toString()
            eq_id = data!!.eq_id.toString()
            eq_name = data!!.equipment.eq_name.toString()
            eqc_name = data!!.equipment.equipment_Type?.eqc_name.toString()
            employeeName = "${data!!.employee.firstname} ${data!!.employee.lastname}"
            department = viewModel.getDepartmentName(data!!.employee.departmentId ?: 0).toString()
            building = data!!.building.building_name
            building_roomnum = data!!.building.building_room_number
            building_floor = data!!.building.building_floor.toString()
            rr_description = data!!.rr_description
            requestStatus = data!!.request_status
            detailTimeStamp = data!!.timestamp
            hasDetail = false
        } else if (data != null && data!!.receive_repair?.repair_details != null) {
            val lastRepairDetail = data!!.receive_repair.repair_details.lastOrNull()
            _rrid = data!!.rrid.toString()
            eq_id = data!!.eq_id.toString()
            eq_name = data!!.equipment.eq_name.toString()
            eqc_name = data!!.equipment.equipment_Type?.eqc_name.toString()
            employeeName = "${data!!.employee.firstname} ${data!!.employee.lastname}"
            technicianName = "${data!!.receive_repair.technician.firstname} ${data!!.receive_repair.technician.lastname}"
            dateReceive = data!!.receive_repair.date_receive.toString()
            if (lastRepairDetail != null) {
                val lastDescription = lastRepairDetail?.rd_description?.split(':') ?: listOf()
                rd_id = lastRepairDetail.rd_id.toString()
                detail = lastRepairDetail.rd_description ?: "ยังไม่กรอกข้อมูล"
                levelOfDamage = lastRepairDetail.levelOfDamage?.loed_Name ?: "ยังไม่กรอกข้อมูล"
                detailTimeStamp = data!!.receive_repair.repair_details.lastOrNull()?.timestamp.toString()
                if(lastDescription.size <= 1){
                    detailDescription = lastRepairDetail?.rd_description.toString()
                }else{
                    detailDescription = lastDescription.getOrNull(1)?.trim() ?: "..."
                }
                hasDetail = true
            } else {
                hasDetail = false
            }
            department = viewModel.getDepartmentName(data!!.employee.departmentId ?: 0).toString()
            building = data!!.building.building_name
            building_roomnum = data!!.building.building_room_number
            building_floor = data!!.building.building_floor.toString()
            rr_description = data!!.rr_description
            requestStatus = data!!.request_status
        }
    }

    if (data == null && imagePainters.isEmpty()) {
        LoadingScreen()
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.padding(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(25.dp))
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color(0xFFD0E6F8))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val typography = MaterialTheme.typography
                    Column(modifier = Modifier.weight(2f)) {
                        Spacer(modifier = Modifier.padding(12.dp))
                        Text(requestStatus, style = typography.subtitle1.copy(color = Color.Blue))
                        if (hasDetail) {
                            Text(formatTimestamp(detailTimeStamp), style = typography.body2.copy(color = Color.DarkGray))
                            Text(detailDescription, style = typography.body2.copy(color = Color.DarkGray))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.Top)
                            .clickable {
                                navController.navigate(ScreenRoutes.statusDetail.passRrid(rrid))
                            }
                    ) {
                        Text("ดูรายละเอียด")
                    }
                }
            }
            Spacer(modifier = Modifier.padding(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(25.dp))
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color(0xFFD0E6F8))
                    .padding(24.dp)
            ) {
                Text(text = "รหัสแจ้งซ่อม : $_rrid", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "อุปกรณ์ : ($eq_id)$eq_name", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ประเภทอุปกรณ์: $eqc_name", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "หน่วยงาน: $department", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "เลขห้อง: $building_roomnum ตึก :$building ชั้น : $building_floor", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ชื่อผู้แจ้ง: $employeeName", fontSize = 18.sp)
                if (hasDetail) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "ชื่อผู้รับงาน: $technicianName", fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "วันที่รับงาน: ${formatTimestamp(dateReceive)} ", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "รายละเอียด :", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color.White)
                        .padding(8.dp),
                    text = rr_description
                )
                if (hasDetail) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "รายละเอียดการซ่อม :", fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.White)
                            .padding(8.dp),
                        text = if(detail.split(':').size > 1) detail.split(':')[1] else detail
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "ระดับความเสียหาย : $levelOfDamage", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "รูปภาพ :", fontSize = 18.sp)
                if (imagePainters.isEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.Gray)
                    ) {
                        Text("No Images", modifier = Modifier.align(Alignment.Center))
                    }
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        imagePainters.forEach { imageBytes ->
                            val bitmap =
                                imageBytes?.let { BitmapFactory.decodeByteArray(imageBytes, 0, it.size) }
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clickable {
                                            selectedImage = imageBytes
                                            showDialog = true
                                        }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            BtnType(userType, hasDetail, navController, admin_id, _rrid, rd_id,requestStatus)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
    if (showDialog) {
        selectedImage?.let { imageBytes ->
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            Dialog(onDismissRequest = { showDialog = false }) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clickable { showDialog = false }
                )
            }
        }
    }
}

@Composable
fun BtnType(
    userType: String,
    hasDetail: Boolean,
    navController: NavController,
    admin_id: String,
    rrid: String,
    rd_id: String,
    requestStatus:String
) {
    var BtnName by remember { mutableStateOf("") }
    var route by remember { mutableStateOf("") }
    var show by remember { mutableStateOf(true) }
    var isUpdate by remember { mutableStateOf(false) }
        when (userType) {
            "Admin" -> {
                if (hasDetail) {
                    show = false
                    isUpdate = true
                }else if(requestStatus != "กำลังส่งการแจ้งซ่อม") {
                    show = false
                    isUpdate = false
                }
                else {
                    show = true
                    isUpdate = false
                    BtnName = "จ่ายงาน"
                    route = ScreenRoutes.TechnicianListBacklog.passAdminIdAndRrid(admin_id, rrid)
                }
            }
            "Technician" -> {
                if(requestStatus != "ส่งคืนเสร็จสิ้น"){
                    if (hasDetail) {
                        BtnName = "อัพเดทงาน"
                        route = ScreenRoutes.addDetailRepair.passRrceIdAndIsUpdate(rrid, rd_id, false)
                        isUpdate = true
                    } else {
                        BtnName = "เพิ่มรายละเอียด"
                        route = ScreenRoutes.addDetailRepair.passRrceIdAndIsUpdate(rrid, rd_id, false)
                        isUpdate = false
                    }
                }else{
                    show = false
                    isUpdate = false
                }
            }
            else -> {
                show = false
                isUpdate = false
            }
        }
    Column(modifier = Modifier.fillMaxSize()) {
    if (show) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFCCFFCC), // Light green background color
                        shape = RoundedCornerShape(16.dp) // Rounded corners
                    )
                    .clickable {
                        navController.navigate(route)
                    }
                    .padding(16.dp), // Padding for the button
                contentAlignment = Alignment.Center // Center the text inside the button
            ) {
                Text(
                    text = BtnName, // Button text
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black // Text color
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    if (isUpdate) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFCCFFCC), // Light green background color
                    shape = RoundedCornerShape(16.dp) // Rounded corners
                )
                .clickable {
                    navController.navigate(
                        ScreenRoutes.addDetailRepair.passRrceIdAndIsUpdate(
                            rrid,
                            rd_id,
                            true
                        )
                    )
                }
                .padding(16.dp), // Padding for the button
            contentAlignment = Alignment.Center // Center the text inside the button
        ) {
            Text(
                text = "แก้ไขข้อมูล", // Button text
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black // Text color
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}