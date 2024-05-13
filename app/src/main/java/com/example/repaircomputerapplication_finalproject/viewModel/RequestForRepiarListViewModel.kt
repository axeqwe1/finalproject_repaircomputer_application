package com.example.repaircomputerapplication_finalproject.viewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.media.audiofx.DynamicsProcessing.Eq
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.`api-service`.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.BuildingData
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.EquipmentData
import com.example.repaircomputerapplication_finalproject.model.RequestForRepairData
import com.example.repaircomputerapplication_finalproject.model.RequestListResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RequestForRepiarListViewModel(application: Application):AndroidViewModel(application){
    val dataStore = application.dataStore
    private var _requestList = MutableStateFlow<List<RequestForRepairData>?>(null)
    var requestList = _requestList.asStateFlow()

    private var _eqtList = MutableStateFlow<List<EquipmentData>?>(null)
    var eqtList = _eqtList.asStateFlow()

    private var _empList = MutableStateFlow<List<EmployeeData>?>(null)
    var empList = _empList.asStateFlow()

    private var _buildingList = MutableStateFlow<List<BuildingData>?>(null)
    var buildingList  = _buildingList.asStateFlow()
    private var role:String = ""
    private var Id:Int? = null

    init {
        loadData()
    }
    fun loadData() {
        viewModelScope.launch {
            try {
                _requestList.value = fetchRequestForRepairList()
                _eqtList.value = fetchEquipmentData()
                _empList.value = fetchEmployeeData()
                _buildingList.value = fetchBuildingData()
                Log.d(TAG, "loadData: ${_requestList.value}")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load data: ${e.message}")
                // จัดการสถานะข้อผิดพลาดที่เหมาะสมที่นี่
            }
        }
    }
    fun getBuildingInfo (buildId:Int):String{
        Log.d(TAG, "getBuildingInfo: ${buildingList.value}")
        buildingList.value?.forEach {items ->
            if(items.building_id == buildId){
                return (items.building_name + " " + items.building_floor + " " + items.building_room_number)
                    ?: "null"
            }
        }
        return "Fail to get Equipment Name"
    }
    fun getEquipmentName (Eq_Id:Int):String{
        eqtList.value?.forEach {items ->
            if(items.eq_id == Eq_Id){
                return items.eq_name ?: "null"
            }
        }
        return "Fail to get Equipment Name"
    }
    fun getEmployeeFullName(Emp_Id:Int):String{
        empList.value?.forEach {items ->
            if(items.emp_id == Emp_Id){
                return items.firstname ?: ""
            }
        }
        return "Fail to get Employee Name"
    }
    suspend fun fetchEmployeeData ():List<EmployeeData>?{
        val response = RetrofitInstance(getApplication()).apiService.getEmployees()
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Fail to Fetch Employee")
        }
    }
    suspend fun fetchEquipmentData ():List<EquipmentData>?{
        val response = RetrofitInstance(getApplication()).apiService.getEquipments()
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Fail to Fetch EquipmentData")
        }
    }
    suspend fun fetchBuildingData ():List<BuildingData>?{
        val response = RetrofitInstance(getApplication()).apiService.getBuildings()
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Fail to Fetch EquipmentData")
        }
    }
    suspend fun fetchRequestForRepairList():List<RequestForRepairData>?{
        role = dataStore.data.map { settings ->
            settings[stringPreferencesKey("role")]
        }.first() ?: "Not Found Role"
        Id = dataStore.data.map { settings ->
            settings[stringPreferencesKey("userId")]
        }.first()?.toInt()
        Log.d(TAG, "fetchRequestForRepairList: $role + $Id")
        val response = RetrofitInstance(getApplication()).apiService.getRequestList(role,Id ?: 0)
        if(response.isSuccessful){
            return response.body()?.data
        }else{
            throw Exception("Failed to fetch Request List")
        }
    }

}