package com.example.repaircomputerapplication_finalproject.viewModel.DisplayViewModel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.AdminData
import com.example.repaircomputerapplication_finalproject.model.detailRepairData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException

class RepairDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _detailData = MutableStateFlow<detailRepairData?>(null)
    val detailData = _detailData.asStateFlow()

    private val _imagePainters = MutableStateFlow<List<ByteArray?>>(emptyList())
    val imagePainters = _imagePainters.asStateFlow()

    private var _adminList = MutableStateFlow<List<AdminData>?>(null)
    var adminList  = _adminList.asStateFlow()

    fun loadData(dataId: String) {
        viewModelScope.launch {
            _detailData.value = fetchRepairDetail(dataId)
            _adminList.value = fetchAdminData()
            _detailData.value?.rr_picture?.let { imageNames ->
                fetchImages(imageNames)
            }
        }
    }
    fun getAssignmentName(adminId:Int):String{
        Log.d(ContentValues.TAG, "getAssignMentName: ${adminList.value}")
        adminList.value?.forEach {items ->
            if(items.admin_id == adminId){
                return items.firstname + " " + items.lastname
            }
        }
        return "Fail to getAssignmentName"
    }
    suspend fun getDepartmentName(dep_id:Int) : String?{
        val response = RetrofitInstance.apiService.getDepartmentById(dep_id)
        if(response.isSuccessful){
            return response.body()?.departmentName
        }else{
            Toast.makeText(getApplication(),"QueryData  Fail",Toast.LENGTH_SHORT).show()
            throw Exception("Fail to Fetch department Data")
        }
    }
    suspend fun fetchAdminData():List<AdminData>?{
        val response = RetrofitInstance.apiService.getAdmins()
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Fail to Fetch EquipmentData")
        }
    }
    private suspend fun fetchRepairDetail(dataId: String): detailRepairData? {
        val response = RetrofitInstance.apiService.getRequestDetail(dataId.toInt())
        return if (response.isSuccessful) {
            response.body()
        } else {
            Toast.makeText(getApplication(), "QueryData  Fail", Toast.LENGTH_SHORT).show()
            throw Exception("Fail to Fetch RepairDetail Data")
        }
    }

    private suspend fun fetchImages(imageNames: String) {
        val imageList = imageNames.split(",").mapNotNull { imageName ->
            fetchImage(imageName)
        }
        _imagePainters.value = imageList
    }

    private suspend fun fetchImage(imageName: String): ByteArray? {
        return try {
            val response = RetrofitInstance.apiService.getImage(imageName)
            if (response.isSuccessful) {
                response.body()?.byteStream()?.use { inputStream ->
                    inputStream.readBytes()
                }
            } else {
                // ถ้าไม่ได้รูปภาพ ให้ส่งค่า null กลับมาและจัดการสถานะที่ไม่ใช่ 200
                null
            }
        } catch (e: HttpException) {
            if (e.code() == 404) {
                // จัดการข้อผิดพลาด 404 (ไม่พบรูปภาพ) โดยการส่งค่า null กลับไป
                null
            } else {
                // จัดการข้อผิดพลาดอื่นๆ ถ้ามี
                e.printStackTrace()
                null
            }
        }
    }

}
