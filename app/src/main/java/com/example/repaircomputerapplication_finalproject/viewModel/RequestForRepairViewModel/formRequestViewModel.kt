package com.example.repaircomputerapplication_finalproject.viewModel.RequestForRepairViewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.BuildingData
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.EquipmentData
import com.example.repaircomputerapplication_finalproject.model.sendRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Locale

class formRequestViewModel(application: Application) : AndroidViewModel(application) {
    private val _buildingResult = MutableStateFlow<List<BuildingData>?>(null)
    val buildingResult = _buildingResult.asStateFlow()
    private val _equipmentResult = MutableStateFlow<List<EquipmentData>?>(null)
    val equipmentResult = _equipmentResult.asStateFlow()
    private val _image1 = MutableStateFlow<Uri?>(null)
    val image1 = _image1.asStateFlow()
    private val _image2 = MutableStateFlow<Uri?>(null)
    val image2 = _image2.asStateFlow()
    private val _image3 = MutableStateFlow<Uri?>(null)
    val image3 = _image3.asStateFlow()
    private val _uploadedFileName1 = MutableStateFlow<String?>(null)
    val uploadedFileName1 = _uploadedFileName1.asStateFlow()
    private val _uploadedFileName2 = MutableStateFlow<String?>(null)
    val uploadedFileName2 = _uploadedFileName2.asStateFlow()
    private val _uploadedFileName3 = MutableStateFlow<String?>(null)
    val uploadedFileName3 = _uploadedFileName3.asStateFlow()
    private val _employeeList = MutableStateFlow<List<EmployeeData>?>(null)
    val employeeList = _employeeList.asStateFlow()
    private val _uploadedStatus1 = MutableStateFlow<Boolean>(false)
    val uploadedStatus1 = _uploadedStatus1.asStateFlow()
    private val _uploadedStatus2 = MutableStateFlow<Boolean>(false)
    val uploadedStatus2 = _uploadedStatus2.asStateFlow()
    private val _uploadedStatus3 = MutableStateFlow<Boolean>(false)
    val uploadedStatus3 = _uploadedStatus3.asStateFlow()
    private val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    private val now = System.currentTimeMillis()
    private val newFileName1 = "Code_" + dateFormat.format(now) + ".jpg"
    private val newFileName2 = "Defective_" + dateFormat.format(now) + ".jpg"
    private val newFileName3 = "Overview_" + dateFormat.format(now) + ".jpg"

    // State for showing the alert dialog
    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> get() = _showErrorDialog

    // State for the error message
    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()
    init {
        loadData()
    }

    suspend fun sendRequest(description: String, employeeId: Int, buildingId: Int, equipmentId: Int) {
        try {
            val response = RetrofitInstance
                .apiService
                .sendRequestForRepair(
                    sendRequest(description, "$newFileName1,$newFileName2,$newFileName3", employeeId, buildingId, equipmentId)
                )
            Log.d(TAG, "SendRequest: ${response.body()}")
            if (response.isSuccessful) {
                Log.d(TAG, "SendRequest: ${response.body()}")
            } else {
                // Extract error message from backend response
                val errorResponse = response.errorBody()?.string()
                _errorMessage.value = errorResponse ?: "เกิดข้อผิดพลาด ไม่สามารถดำเนินการได้"
                _showErrorDialog.value = true
                Log.d(TAG, "errorResponse: ${errorResponse} \n boolean is ${_showErrorDialog.value}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "SendRequest: ${e}")
            _errorMessage.value = "เกิดข้อผิดพลาด: ${e.message}"
            _showErrorDialog.value = true
        }
    }

    // Function to reset the dialog state
    fun resetErrorDialog() {
        _showErrorDialog.value = false
        _errorMessage.value = ""
    }
    private fun loadData() {
        viewModelScope.launch {
            try {
                _equipmentResult.value = fetchEquipmentData()
                _buildingResult.value = fetchBuildingData()
                _employeeList.value = fetchEmployee()
            } catch (e: Exception) {
                _buildingResult.value = emptyList()
            }
        }
    }

    fun saveImageState(image: Uri, imageIndex: Int) {
        when (imageIndex) {
            1 -> _image1.value = image
            2 -> _image2.value = image
            3 -> _image3.value = image
        }
    }

    suspend fun fetchBuildingData(): List<BuildingData>? {
        val response = RetrofitInstance.apiService.getBuildings()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("Failed to fetch buildings")
        }
    }

    suspend fun fetchEquipmentData(): List<EquipmentData>? {
        val response = RetrofitInstance.apiService.getEquipments()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("Failed to fetch equipment")
        }
    }

    suspend fun fetchEmployee(): List<EmployeeData>? {
        val response = RetrofitInstance.apiService.getEmployees()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception("Fail to fetch Employee")
        }
    }

    fun getFileName(imageIndex: Int): String {
        return when (imageIndex) {
            1 -> uploadedFileName1.value ?: ""
            2 -> uploadedFileName2.value ?: ""
            3 -> uploadedFileName3.value ?: ""
            else -> ""
        }
    }

    suspend fun uploadImage(imageUri: Uri, imageIndex: Int) {
        try {
            val inputStream = getApplication<Application>().contentResolver.openInputStream(imageUri)
            val imageBytes = inputStream?.readBytes()
            inputStream?.close()
            val newFileName = when (imageIndex) {
                1 -> newFileName1
                2 -> newFileName2
                3 -> newFileName3
                else -> ""
            }
            val filePart = MultipartBody.Part.createFormData(
                "image",
                newFileName,
                imageBytes!!.toRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            val response = RetrofitInstance.apiService.uploadImage(filePart)
            Log.d(TAG, "uploadImage: ${response.isSuccessful}")
            if (response.isSuccessful) {
                when (imageIndex) {
                    1 -> {
                        _uploadedFileName1.value = newFileName
                        _uploadedStatus1.value = true
                    }
                    2 -> {
                        _uploadedFileName2.value = newFileName
                        _uploadedStatus2.value = true
                    }
                    3 -> {
                        _uploadedFileName3.value = newFileName
                        _uploadedStatus3.value = true
                    }
                }
            } else {
                when (imageIndex) {
                    1 -> _uploadedStatus1.value = false
                    2 -> _uploadedStatus2.value = false
                    3 -> _uploadedStatus3.value = false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "uploadImage: ${e}")
            when (imageIndex) {
                1 -> _uploadedStatus1.value = false
                2 -> _uploadedStatus2.value = false
                3 -> _uploadedStatus3.value = false
            }
        }
    }
}
