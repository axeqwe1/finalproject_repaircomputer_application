import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repaircomputerapplication_finalproject.model.RepairStatus

@Composable
fun TrackRequestStatusScreen(combinedStatus: List<RepairStatus>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        combinedStatus.forEachIndexed { index, status ->
            RepairStatusItem(status, index, combinedStatus.size)
        }
    }
}

@Composable
fun RepairStatusItem(status: RepairStatus, index: Int, totalItems: Int) {
    val (color, icon) = getColorAndIcon(status.status)
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (index > 0) {
                    Divider(
                        color = color,
                        modifier = Modifier
                            .width(2.dp)
                            .height(20.dp)
                    )
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = color.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )
                if (index < totalItems - 1) {
                    Divider(
                        color = color,
                        modifier = Modifier
                            .width(2.dp)
                            .height(30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = status.status,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                status.subStatus.forEach { subStatus ->
                    Text(
                        text = subStatus,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = status.timestamp,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

fun getColorAndIcon(status: String): Pair<Color, androidx.compose.ui.graphics.vector.ImageVector> {
    return when (status) {
        "กำลังส่งการแจ้งซ่อม" -> Pair(Color.Yellow, Icons.Default.Email)
        "กำลังดำเนินการ" -> Pair(Color.Yellow, Icons.Default.Build)
        "เสร็จสิ้นแล้ว" -> Pair(Color.Green, Icons.Default.Check)
        "ไม่พบว่าชำรุด" -> Pair(Color.Blue, Icons.Default.Remove)
        "ไม่สามารถซ่อมได้" -> Pair(Color.Red, Icons.Default.Close)
        else -> Pair(Color.Gray, Icons.Default.Info)
    }
}

@Preview(showBackground = true)
@Composable
fun TrackRequestStatusScreenPreview() {
    val repairStatuses = listOf(
        RepairStatus("03-03-2024 11:02", "กำลังส่งการแจ้งซ่อม"),
        RepairStatus("03-03-2024 08:33", "กำลังดำเนินการ", listOf("จ่ายไฟ")),
        RepairStatus("02-03-2024 17:48", "กำลังดำเนินการ", listOf("เปลี่ยน Board")),
        RepairStatus("02-03-2024 15:56", "เสร็จสิ้นแล้ว", listOf("รับคืนโดย: นายแมว แววตา")),
        RepairStatus("02-03-2024 08:47", "ไม่สามารถซ่อมได้", listOf("เปลี่ยนอุปกรณ์ให้ใหม่")),
        RepairStatus("01-03-2024 12:46", "ไม่พบว่าชำรุด", listOf("รับคืนโดย: นายแมว แววตา"))
    )
    TrackRequestStatusScreen(repairStatuses)
}