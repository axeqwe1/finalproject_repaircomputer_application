package com.example.repaircomputerapplication_finalproject.viewModel.RequestForRepairViewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.BuildingData
import com.example.repaircomputerapplication_finalproject.model.DepartmentData
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.EquipmentData
import com.example.repaircomputerapplication_finalproject.model.TechnicianData
import com.example.repaircomputerapplication_finalproject.model.detailRepairData
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RequestForRepiarListViewModel(application: Application):AndroidViewModel(application){
    val dataStore = application.dataStore

    private var _requestList = MutableStateFlow<List<detailRepairData>?>(null)
    var requestList = _requestList.asStateFlow()

    private var _eqtList = MutableStateFlow<List<EquipmentData>?>(null)
    var eqtList = _eqtList.asStateFlow()

    private var _empList = MutableStateFlow<List<EmployeeData>?>(null)
    var empList = _empList.asStateFlow()

    private var _techList = MutableStateFlow<List<TechnicianData>?>(null)
    var techList = _techList.asStateFlow()

    private var _buildingList = MutableStateFlow<List<BuildingData>?>(null)
    var buildingList  = _buildingList.asStateFlow()

    private var _departList = MutableStateFlow<List<DepartmentData>?>(null)
    var departList  = _departList.asStateFlow()

    private var role:String = ""
    private var Id:Int? = null

    fun loadData() {
        viewModelScope.launch {
            try {
                _requestList.value = fetchRequestForRepairList()
                _eqtList.value = fetchEquipmentData()
                _empList.value = fetchEmployeeData()
                _techList.value = fetchTechnicianData()
                _buildingList.value = fetchBuildingData()
                _departList.value = fetchDepartmentData()
                Log.d(TAG, "loadData: ${_requestList.value}")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load data: ${e.message}")
                // จัดการสถานะข้อผิดพลาดที่เหมาะสมที่นี่
            }
        }
    }

    fun getBuildingName (buildId:Int):String{
        Log.d(TAG, "getBuildingInfo: ${buildingList.value}")
        buildingList.value?.forEach {items ->
            if(items.building_id == buildId){
                return items.building_name
            }
        }
        return "Fail to getBuildingName"
    }
    fun getBuildingFloor (buildId:Int):String{
        Log.d(TAG, "getBuildingInfo: ${buildingList.value}")
        buildingList.value?.forEach {items ->
            if(items.building_id == buildId){
                return items.building_floor.toString()
            }
        }
        return "Fail to getBuildingFloor"
    }
    fun getBuildingRoom (buildId:Int):String{
        Log.d(TAG, "getBuildingInfo: ${buildingList.value}")
        buildingList.value?.forEach {items ->
            if(items.building_id == buildId){
                return items.building_room_number
            }
        }
        return "Fail to getBuildingRoom"
    }

    fun getEmployeeFullName(Emp_Id:Int):String{
        empList.value?.forEach {items ->
            if(items.emp_id == Emp_Id){
                return items.firstname ?: ""
            }
        }
        return "Fail to get Employee Name"
    }
    fun getDepartmentName(depart_Id: Int):String{
        departList.value?.forEach{items ->
            if(items.department_id == depart_Id){
                return items.departmentName
            }
        }
        return "Fail to getDepartmentName"
    }
    fun getTechnicianName(tech_id: Int):String{
        techList.value?.forEach{items ->
            if(items.tech_id == tech_id){
                return "${items.firstname}  ${items.lastname}"
            }
        }
        return "Fail to getDepartmentName"
    }
    suspend fun fetchTechnicianData ():List<TechnicianData>?{
        val response = RetrofitInstance.apiService.getTechnicians()
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Fail to Fetch Employee")
        }
    }
    suspend fun fetchDepartmentData ():List<DepartmentData>?{
        val response = RetrofitInstance.apiService.getDepartments()
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Fail to Fetch Employee")
        }
    }
    suspend fun fetchEmployeeData ():List<EmployeeData>?{
        val response = RetrofitInstance.apiService.getEmployees()
        Log.d(TAG, "fetchEmployeeData: ${response}")
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Fail to Fetch Employee")
        }
    }
    suspend fun fetchEquipmentData ():List<EquipmentData>?{
        val response = RetrofitInstance.apiService.getEquipments()
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Fail to Fetch EquipmentData")
        }
    }
    suspend fun fetchBuildingData ():List<BuildingData>?{
        val response = RetrofitInstance.apiService.getBuildings()
        if(response.isSuccessful){
            return response.body()
        }else{
            throw Exception("Fail to Fetch EquipmentData")
        }
    }
    suspend fun fetchRequestForRepairList():List<detailRepairData>?{
        role = dataStore.data.map { settings ->
            settings[stringPreferencesKey("role")]
        }.first() ?: "Not Found Role"
        Id = dataStore.data.map { settings ->
            settings[stringPreferencesKey("userId")]
        }.first()?.toInt()
        Log.d(TAG, "fetchRequestForRepairList: $role + $Id")
        val response = RetrofitInstance.apiService.getRequestList(role,Id ?: 0)
        if(response.isSuccessful){
            return response.body()?.data
        }else{
            throw Exception("Failed to fetch Request List")
        }
    }


}