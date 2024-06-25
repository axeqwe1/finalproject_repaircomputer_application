package com.example.repaircomputerapplication_finalproject.viewModel.RequestForRepairViewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.`api-service`.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.BuildingData
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.EquipmentData
import com.example.repaircomputerapplication_finalproject.model.sendRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Locale

class formRequestViewModel(application: Application):AndroidViewModel(application) {
    private val _buildingResult = MutableStateFlow<List<BuildingData>?>(null)
    val buildingResult = _buildingResult.asStateFlow()
    private val _equipmentResult = MutableStateFlow<List<EquipmentData>?>(null)
    val equipmentResult = _equipmentResult.asStateFlow()
    private val _image = MutableStateFlow<Uri?>(null)
    val image = _image.asStateFlow()
    private val _uploadedFileName = MutableStateFlow<String?>(null)
    val uploadedFileName = _uploadedFileName.asStateFlow()
    private val _employeeList = MutableStateFlow<List<EmployeeData>?>(null)
    val employeeList = _employeeList.asStateFlow()
    private val _uploadedStatus = MutableStateFlow<Boolean>(false)
    val uploadedStatus = _uploadedStatus.asStateFlow()
    private val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    private val now = System.currentTimeMillis()
    private val newFileName = dateFormat.format(now) + ".jpg"
    init {
        loadData()
    }

    fun SendRequest(description:String,picture:String,employeeId:Int,buildingId:Int,equipmentId:Int){
        viewModelScope.launch {
            try {
                val response = RetrofitInstance(getApplication())
                    .apiService
                    .sendRequestForRepair(
                        sendRequest(description,newFileName,employeeId,buildingId,equipmentId)
                    )
                Log.d(TAG, "SendRequest: ${response.body()}")
                if(response.isSuccessful){
                    Log.d(TAG, "SendRequest: ${response.body()}")

                }
            }catch (e:Exception){
                Log.e(TAG, "SendRequest: ${e}")
            }
        }
    }
    private fun loadData(){
        viewModelScope.launch {
            try {
                _equipmentResult.value = fetchEquipmentData()
                _buildingResult.value = fetchBuildingData()
                _employeeList.value = fetchEmployee()
            }catch (e:Exception){
                _buildingResult.value = emptyList()
            }
        }
    }
    fun saveImageState(image: Uri){
        _image.value = image
    }

    suspend fun fetchBuildingData() : List<BuildingData>? {
        val response = RetrofitInstance(getApplication()).apiService.getBuildings()
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Failed to fetch buildings")
        }
    }

    suspend fun fetchEquipmentData(): List<EquipmentData>?{
        val response = RetrofitInstance(getApplication()).apiService.getEquipments()
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Failed to fetch equipment")
        }
    }

    suspend fun fetchEmployee(): List<EmployeeData>?{
        val response = RetrofitInstance(getApplication()).apiService.getEmployees()
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Fail to fetch Employee")
        }
    }
    fun getFileName():String{
        return uploadedFileName.value ?: ""
    }
    fun uploadImage(imageUri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream = getApplication<Application>().contentResolver.openInputStream(imageUri)
                val imageBytes = inputStream?.readBytes()
                inputStream?.close()
                val filePart = MultipartBody.Part.createFormData(
                    "image", // ชื่อสำหรับ part
                    newFileName,
                    imageBytes!!.toRequestBody()
                )
                // Assuming RetrofitInstance is set up for your server's base URL
                val response = RetrofitInstance(getApplication()).apiService.uploadImage(filePart)
                Log.d(TAG, "uploadImage: ${response.isSuccessful}")
                if (response.isSuccessful) {
                    _uploadedFileName.value = newFileName
                    Log.d(TAG, "uploadImage: ${uploadedFileName.value}")
                    _uploadedStatus.value = true
                    // Optionally save filename to your ViewModel if needed elsewhere
                } else {
                    _uploadedStatus.value = false
                }
            } catch (e: Exception) {


            }
        }
    }
}