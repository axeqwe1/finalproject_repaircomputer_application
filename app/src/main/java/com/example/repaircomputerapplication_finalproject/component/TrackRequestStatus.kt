import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class RepairStatus(
    val timestamp: String,
    val status: String,
    val subStatus: List<String> = emptyList()
)
data class RepairRequest(
    val id: Int,
    val description: String,
    val status: String,
    val timestamp: String
)
@Composable
fun TrackRequestStatusScreen() {
    val repairRequests = listOf(
        RepairRequest(1, "Fix the air conditioner", "In Progress", "03-03-2024 11:02"),
        RepairRequest(2, "Repair the door lock", "Completed", "02-03-2024 15:56"),
        RepairRequest(3, "Replace the light bulb", "Pending", "02-03-2024 08:47"),
        RepairRequest(4, "Fix the plumbing issue", "Completed", "01-03-2024 17:48"),
        RepairRequest(5, "Check the electrical wiring", "In Progress", "01-03-2024 12:33")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Track Request Status", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 16.dp))
        repairRequests.forEach { request ->
            RepairRequestItem(request)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RepairRequestItem(request: RepairRequest) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = request.description, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Status: ${request.status}", fontSize = 14.sp)
            Text(text = "Timestamp: ${request.timestamp}", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrackRequestStatusScreenPreview() {
    TrackRequestStatusScreen()
}
