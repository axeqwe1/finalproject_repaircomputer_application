package com.example.repaircomputerapplication_finalproject.viewModel.DisplayViewModel

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.BuildingData
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.RequestForRepairData
import com.example.repaircomputerapplication_finalproject.model.RequestResponse
import com.example.repaircomputerapplication_finalproject.model.detailRepairData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.ResponseBody

class RepairDetailViewModel(application: Application) : AndroidViewModel(application){

    private val _detailData = MutableStateFlow<detailRepairData?>(null)
    val detailData = _detailData.asStateFlow()

    private val _imagePainter = MutableStateFlow<ByteArray?>(null)
    val imagePainter = _imagePainter.asStateFlow()

    fun LoadData(dataId:String){
        viewModelScope.launch {
            _detailData.value = fetchRepairDetail(dataId)
        }
    }
   suspend fun fetchRepairDetail(dataId:String) : detailRepairData?{
        val response = RetrofitInstance.apiService.getRequestDetail(dataId.toInt())
        if(response.isSuccessful){
            return response.body()
        }else{
            Toast.makeText(getApplication(),"QueryData  Fail",Toast.LENGTH_SHORT).show()
            throw Exception("Fail to Fetch RepairDetail Data")
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
    fun fetchImage(imageName: String) {
        viewModelScope.launch {
            val response: ResponseBody = RetrofitInstance.apiService.getImage(imageName)
            val inputStream = response.byteStream()
            val data = inputStream.readBytes()
            inputStream.close()
            withContext(Dispatchers.Main) {
                _imagePainter.value = data
            }
        }
    }
}