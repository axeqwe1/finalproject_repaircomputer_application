package com.example.repaircomputerapplication_finalproject.viewModel.DisplayViewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.detailRepairData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class RepairDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _detailData = MutableStateFlow<detailRepairData?>(null)
    val detailData = _detailData.asStateFlow()

    private val _imagePainters = MutableStateFlow<List<ByteArray>>(emptyList())
    val imagePainters = _imagePainters.asStateFlow()

    fun loadData(dataId: String) {
        viewModelScope.launch {
            _detailData.value = fetchRepairDetail(dataId)
            _detailData.value?.rr_picture?.let { imageNames ->
                fetchImages(imageNames)
            }
        }
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
        val imageList = imageNames.split(",").map { imageName ->
            fetchImage(imageName)
        }
        _imagePainters.value = imageList
    }

    private suspend fun fetchImage(imageName: String): ByteArray {
        val response: ResponseBody = RetrofitInstance.apiService.getImage(imageName)
        val inputStream = response.byteStream()
        val data = inputStream.readBytes()
        inputStream.close()
        return data
    }
}
