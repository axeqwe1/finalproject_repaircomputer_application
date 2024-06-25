
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.screens.LoadingScreen
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import com.example.repaircomputerapplication_finalproject.viewModel.DisplayViewModel.RepairDetailViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


@Composable
fun DetailRepairScreen(rrid:String, userType:String, navController: NavController,viewModel:RepairDetailViewModel = viewModel()) {
    val data = viewModel.detailData.collectAsState().value
    val imageData by viewModel.imagePainter.collectAsState()
    var _rrid by remember { mutableStateOf("") }
    var eq_id by remember { mutableStateOf("") }
    var eq_name by remember { mutableStateOf("") }
    var eqc_name by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var building by remember { mutableStateOf("") }
    var building_roomnum by remember { mutableStateOf("") }
    var building_floor by remember { mutableStateOf("") }
    var employeeName by remember { mutableStateOf("") }
    var rr_description by remember{ mutableStateOf("") }
    var hasDetail by remember { mutableStateOf(false) }
    var technicianName by remember{ mutableStateOf("") }
    var dateReceive by remember{ mutableStateOf("") }
    var detail by remember{ mutableStateOf("ยังไม่กรอกข้อมูล") }
    var levelOfDamage by remember{ mutableStateOf("ยังไม่กรอกข้อมูล") }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var admin_id by remember{ mutableStateOf("") }
    var rd_id by remember { mutableStateOf("") }
    LaunchedEffect(data){
        if(userType == "Admin"){
            admin_id = context.dataStore.data.map { item ->
                item[stringPreferencesKey("userId")]
            }.first() ?: "null"
        }else{
            admin_id = "null"
        }
        viewModel.LoadData(rrid)
        if(data != null && data.receive_repair == null && data.assign_work == null){
            // ไม่มีDetail ในการรับงาน
            viewModel.fetchImage(data.rr_picture)
            _rrid = data.rrid.toString()
            eq_id = data.eq_id.toString()
            eq_name = data.equipment.eq_name.toString()
            eqc_name = data.equipment.equipment_Type?.eqc_name.toString()
            employeeName = "${data.employee.firstname}  ${data.employee.lastname}"
            department = viewModel.getDepartmentName(data.employee.departmentId ?: 0).toString()
            building = data.building.building_name
            building_roomnum = data.building.building_room_number
            building_floor = data.building.building_floor.toString()
            rr_description = data.rr_description
            Log.d(TAG, "DetailRepairScreen: $data")
            hasDetail = false
        }else if(data != null && data.receive_repair.repair_details != null)
        {
            // มีDetail ในการรับงาน
            val lastRepairDetail = data.receive_repair.repair_details.lastOrNull()
            viewModel.fetchImage(data.rr_picture)
            _rrid = data.rrid.toString()
            eq_id = data.eq_id.toString()
            eq_name = data.equipment.eq_name.toString()
            eqc_name = data.equipment.equipment_Type?.eqc_name.toString()
            employeeName = "${data.employee.firstname}  ${data.employee.lastname}"
            technicianName = "${data.receive_repair.technician.firstname}   ${data.receive_repair.technician.lastname}"
            dateReceive = "${data.receive_repair.date_receive}"
            if(lastRepairDetail != null){
                rd_id = lastRepairDetail.rd_id.toString()
                detail = lastRepairDetail.rd_description ?: "ยังไม่กรอกข้อมูล"
                levelOfDamage = lastRepairDetail.levelOfDamage?.loed_Name ?: "ยังไม่กรอกข้อมูล"
                hasDetail = true
            }else{
                hasDetail = false
            }
            department = viewModel.getDepartmentName(data.employee.departmentId ?: 0).toString()
            building = data.building.building_name
            building_roomnum = data.building.building_room_number
            building_floor = data.building.building_floor.toString()
            rr_description = data.rr_description
            Log.d(TAG, "DetailRepairScreen: $data")
        }else{
            Log.d(TAG, "DetailRepairScreen: $data")
        }
    }
    if(data == null && imageData == null){
        LoadingScreen()
    }else{
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
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box() {
                        Text(text = "ดูรายละเอียด")
                    }
                }
            }
            Spacer(modifier = Modifier.padding(24.dp))
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
                    Text(text = "ชื่อผู้รับงาน: $technicianName", fontSize = 18.sp) // รับงาน
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "วันที่รับงาน: $dateReceive", fontSize = 18.sp) // รับงาน
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
                        text = detail
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "ระดับความเสียหาย : $levelOfDamage", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "รูปภาพ :", fontSize = 18.sp)
                if(imageData == null){
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .background(Color.Gray)
                    ) {
                        // Image content will be placed here
                    }
                }else{
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = imageData,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            BtnType(userType,hasDetail,navController,admin_id,_rrid,rd_id)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BtnType(userType: String,hasDetail:Boolean,navController: NavController,admin_id:String,rrid: String,rd_id:String) {
    var BtnName by remember { mutableStateOf("") }
    var route by remember{ mutableStateOf("") }
    var show by remember{ mutableStateOf(true) }
    var isUpdate by remember{ mutableStateOf(false) }
    when (userType) {
        "Admin" -> {
            if(hasDetail){
                show = false
            }else{
                BtnName = "จ่ายงาน"
                route = ScreenRoutes.TechnicianListBacklog.passAdminIdAndRrid(admin_id,rrid)
            }
        }
        "Technician" -> {
            if (hasDetail) {
                BtnName = "อัพเดทงาน"
                route = ScreenRoutes.addDetailRepair.passRrceIdAndIsUpdate(rrid,rd_id,false)
                isUpdate = true
            }else{
                BtnName = "กรอกรายละเอียด"
                route = ScreenRoutes.addDetailRepair.passRrceIdAndIsUpdate(rrid,rd_id,false)
                isUpdate = false
            }
        }
        else -> show = false
    }
    if(show){
        Column(modifier = Modifier.fillMaxSize()) {
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
                    text = "$BtnName", // Button text
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black // Text color
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if(isUpdate){
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
    }
}
