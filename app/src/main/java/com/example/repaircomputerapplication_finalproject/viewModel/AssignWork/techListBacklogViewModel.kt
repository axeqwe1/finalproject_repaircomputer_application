package com.example.repaircomputerapplication_finalproject.viewModel.AssignWork

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.AssignWorkBody
import com.example.repaircomputerapplication_finalproject.model.TechBacklogCount
import com.example.repaircomputerapplication_finalproject.model.TechnicianData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class techListBacklogViewModel(application: Application):AndroidViewModel(application) {
    private val _techList = MutableStateFlow<List<TechnicianData>?>(null)
    val techList = _techList.asStateFlow()


    init {
        LoadData()
    }

    fun LoadData(){
        viewModelScope.launch {
            _techList.value = fetchTechData()
        }
    }

    fun assignWork(admin_id:Int,rrid:Int,tech_id: Int){
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.assignWork(AssignWorkBody(admin_id,tech_id,rrid))
            if(response.isSuccessful && response.body()?.message == "จ่ายงานสำเร็จ"){
                Toast.makeText(getApplication(),"จ่ายงานสำเร็จ",Toast.LENGTH_SHORT).show()
            }else{
                throw Exception("Fail to assignWork")
            }
        }
    }

    suspend fun getTechName(tech_id:Int):String{
        val response = RetrofitInstance.apiService.getTechnicianById(tech_id)
        if(response.isSuccessful && response.body() != null){
            return "${response.body()?.firstname}  ${response.body()?.firstname}"
        }else{
            throw Exception("Fail to Get TechName")
        }
    }

    suspend fun getTechnicianStatus(statusId: Int):String{
        val response = RetrofitInstance.apiService.getTechStatusById(statusId)
        if(response.isSuccessful && response.body() != null){
            return "${response.body()?.receive_request_status}"
        }else{
            throw Exception("Fail to Get TechName")
        }
    }

    suspend fun fetchTechData():List<TechnicianData> {
        val response = RetrofitInstance.apiService.getTechnicians()
        if(response.isSuccessful && response.body() != null){
            return response.body()!!
        }else{
            throw Exception("Fail to fetch TechData")
        }
    }

    suspend fun fetchTechBacklog(tech_id:Int) : TechBacklogCount {
        val response = RetrofitInstance.apiService.getTechBacklogCount(tech_id)
        if(response.isSuccessful && response.body() != null){
            return response.body()!!
        }else{
            throw Exception("Fail to Fetch BackLogCoung Technician")
        }
    }
}