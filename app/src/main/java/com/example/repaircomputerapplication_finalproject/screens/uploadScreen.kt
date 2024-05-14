import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberImagePainter
import java.util.Objects
import java.util.jar.Manifest
import com.example.repaircomputerapplication_finalproject.R
import com.example.repaircomputerapplication_finalproject.viewModel.formRequestViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun ImageUploadScreen(viewModel: formRequestViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity
    val file = activity!!.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        activity.packageName + ".provider",file
    )

    val uploadedFileName by viewModel.uploadedFileName.collectAsState()

    var captureImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // ถ้าผู้ใช้ถ่ายภาพและยืนยัน, ให้อัปเดต URI ที่แสดง
            captureImageUri = uri
            viewModel.saveImageState(captureImageUri)
        } else {
            // ถ้าผู้ใช้ยกเลิกการถ่ายภาพ, ควรรีเซ็ต URI หรือจัดการอื่นๆ ตามที่ต้องการ
            captureImageUri = Uri.EMPTY
            viewModel.saveImageState(captureImageUri)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if(it){
            Toast.makeText(activity,"Permission Granted",Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        }else{
            Toast.makeText(activity,"Permission Denied",Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        Button(onClick = {
            val permissionCheckResult = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)

            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)  // เรียกใช้กล้องเมื่อมีสิทธิ์อยู่แล้ว
            } else {
                permissionLauncher.launch(android.Manifest.permission.CAMERA)  // ขอสิทธิ์กล้องถ้ายังไม่ได้รับ
            }
        }) {
            Text(text = "Capture Image")
        }

        Spacer(modifier = Modifier.height(16.dp))
        if(captureImageUri.path?.isNotEmpty() == true)
        {
            viewModel.saveImageState(captureImageUri)
            Log.d(TAG, "ImageUploadScreen:${captureImageUri} ")
            Image(
                modifier = Modifier.padding(16.dp),
                painter = rememberImagePainter(captureImageUri),
                contentDescription = null
            )
        }else
        {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("รูปภาพที่อัพโหลด")
            }
        }
    }
}

fun Activity.createImageFile():File{

    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image =File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}



