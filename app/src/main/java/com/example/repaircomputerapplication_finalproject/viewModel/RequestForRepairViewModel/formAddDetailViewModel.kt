package com.example.repaircomputerapplication_finalproject.viewModel.RequestForRepairViewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.LevelOfDamageData
import com.example.repaircomputerapplication_finalproject.model.RequestForRepairData
import com.example.repaircomputerapplication_finalproject.model.addDetailRequest
import com.example.repaircomputerapplication_finalproject.model.detailRepairData
import com.example.repaircomputerapplication_finalproject.model.updateDetailRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class formAddDetailViewModel(application: Application):AndroidViewModel(application){
    private val _loedData = MutableStateFlow<List<LevelOfDamageData>?>(null)
    val loedData = _loedData.asStateFlow()

    init {
        LoadData()
    }
    fun LoadData(){
        viewModelScope.launch {
            _loedData.value = fetchLevelOfDamage()
        }
    }
    fun UpdateDetail(rd_id:Int,loed_id: String,rd_description: String,request_status:String){
        viewModelScope.launch {
            Log.d(TAG, "UpdateDetail: $rd_id $loed_id $rd_description $request_status")
            val response = RetrofitInstance.apiService.updateDetail(
                rd_id,
                updateDetailRequest(loed_id.toInt(),rd_description,request_status)
            )
            if(response.isSuccessful){
                Toast.makeText(getApplication(),"${response.body()}",Toast.LENGTH_SHORT)
            }else{
                throw Exception("${response.errorBody()}")
            }
        }
    }
    fun AddDetail(rrce_id:String,loed_id:String,rd_description:String,request_status: String){
        viewModelScope.launch {
            val response
            = RetrofitInstance.apiService
                .addDetail(addDetailRequest(
                    loed_id = loed_id.toInt(),
                    rrce_id = rrce_id.toInt(),
                    rd_description = rd_description,
                    request_status = request_status
                ))
            if(response.isSuccessful ){
                Log.d(TAG, "AddDetail: ${response.body()}")
            }else{
                throw Exception("Fail to Add Detail Data")
            }
        }
    }
    fun getDepartmentName(departId:Int):String{
        var departName:String = ""
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.getDepartmentById(departId)
            if(response.isSuccessful && response.body() != null){
                departName = response.body()!!.departmentName
            }
        }
        return departName
    }
    suspend fun fetchRequestForRepairData(rrid:Int):detailRepairData{
        val response = RetrofitInstance.apiService.getRequestDetail(rrid)
        if(response.isSuccessful && response.body() != null){
            return response.body()!!
        }else{
            throw Exception("Fail to Fetch Level Of Damage Data")
        }
    }
    suspend fun fetchLevelOfDamage():List<LevelOfDamageData>?{
        val response = RetrofitInstance.apiService.getLevelOfDamages()
        if(response.isSuccessful && response.body() != null){
            return response.body()
        }else{
            throw Exception("Fail to Fetch Level Of Damage Data")
        }
    }
}