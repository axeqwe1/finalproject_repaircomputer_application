import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.repaircomputerapplication_finalproject.viewModel.RequestForRepairViewModel.formRequestViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
fun ImageUploadScreen(viewModel: formRequestViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity

    var captureImageUri1 by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var captureImageUri2 by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var captureImageUri3 by remember { mutableStateOf<Uri>(Uri.EMPTY) }

    val imageFile1 = activity!!.createImageFile("Code")
    val uri1 = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        activity.packageName + ".provider", imageFile1
    )

    val imageFile2 = activity.createImageFile("Defective")
    val uri2 = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        activity.packageName + ".provider", imageFile2
    )

    val imageFile3 = activity.createImageFile("Overview")
    val uri3 = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        activity.packageName + ".provider", imageFile3
    )

    val cameraLauncher1 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            captureImageUri1 = uri1
            viewModel.saveImageState(captureImageUri1, 1)
        }
    }

    val cameraLauncher2 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            captureImageUri2 = uri2
            viewModel.saveImageState(captureImageUri2, 2)
        }
    }

    val cameraLauncher3 = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            captureImageUri3 = uri3
            viewModel.saveImageState(captureImageUri3, 3)
        }
    }

    val galleryLauncher1 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            captureImageUri1 = uri
            viewModel.saveImageState(captureImageUri1, 1)
        }
    }

    val galleryLauncher2 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            captureImageUri2 = uri
            viewModel.saveImageState(captureImageUri2, 2)
        }
    }

    val galleryLauncher3 = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            captureImageUri3 = uri
            viewModel.saveImageState(captureImageUri3, 3)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(Color(0xFFB3E5FC)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UploadImageSection(
            "รูปรหัสอุปกรณ์",
            captureImageUri1,
            { checkCameraPermissionAndLaunch(permissionLauncher, cameraLauncher1, activity, uri1) },
            { galleryLauncher1.launch("image/*") },
            "Capture",
            "Select"
        )

        UploadImageSection(
            "รูปอาการเสีย",
            captureImageUri2,
            { checkCameraPermissionAndLaunch(permissionLauncher, cameraLauncher2, activity, uri2) },
            { galleryLauncher2.launch("image/*") },
            "Capture",
            "Select"
        )

        UploadImageSection(
            "รูปอุปกรณ์",
            captureImageUri3,
            { checkCameraPermissionAndLaunch(permissionLauncher, cameraLauncher3, activity, uri3) },
            { galleryLauncher3.launch("image/*") },
            "Capture",
            "Select"
        )
    }
}

@Composable
fun UploadImageSection(
    title: String,
    captureImageUri: Uri,
    onCaptureClick: () -> Unit,
    onSelectClick: () -> Unit,
    captureButtonText: String,
    selectButtonText: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = title, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Button(onClick = onCaptureClick) {
                    Text(text = captureButtonText)
                }

                Button(onClick = onSelectClick) {
                    Text(text = selectButtonText)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (captureImageUri.path?.isNotEmpty() == true) {
                AsyncImage(
                    model = captureImageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(240.dp)
                        .background(Color.Gray)
                        .padding(8.dp)
                )
            } else {
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
}

fun Activity.createImageFile(prefix: String): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "${prefix}_${timeStamp}_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}

fun checkCameraPermissionAndLaunch(
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    activity: Activity,
    uri: Uri
) {
    if (ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        permissionLauncher.launch(android.Manifest.permission.CAMERA)
    } else {
        cameraLauncher.launch(uri)
    }
}