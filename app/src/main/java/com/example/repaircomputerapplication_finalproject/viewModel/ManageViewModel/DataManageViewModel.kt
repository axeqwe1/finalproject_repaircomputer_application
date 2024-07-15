package com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.AdminData
import com.example.repaircomputerapplication_finalproject.model.BuildingData
import com.example.repaircomputerapplication_finalproject.model.BuildingRequest
import com.example.repaircomputerapplication_finalproject.model.ChiefData
import com.example.repaircomputerapplication_finalproject.model.DepartmentData
import com.example.repaircomputerapplication_finalproject.model.DepartmentRequest
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.EquipmentData
import com.example.repaircomputerapplication_finalproject.model.EquipmentRequest
import com.example.repaircomputerapplication_finalproject.model.EquipmentTypeData
import com.example.repaircomputerapplication_finalproject.model.EquipmentTypeRequest
import com.example.repaircomputerapplication_finalproject.model.LevelOfDamageData
import com.example.repaircomputerapplication_finalproject.model.LevelOfDamageRequest
import com.example.repaircomputerapplication_finalproject.model.TechnicianData
import com.example.repaircomputerapplication_finalproject.model.techStatusData
import com.example.repaircomputerapplication_finalproject.model.techStatusRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import retrofit2.Response

class DataManageViewModel(application: Application):AndroidViewModel(application) {

    private val _building = MutableStateFlow<List<BuildingData>?>(null)
    val building = _building.asStateFlow()

    private val _department = MutableStateFlow<List<DepartmentData>?>(null)
    val department = _department.asStateFlow()

    private val _eq = MutableStateFlow<List<EquipmentData>?>(null)
    val eq = _eq.asStateFlow()

    private val _eqc = MutableStateFlow<List<EquipmentTypeData>?>(null)
    val eqc = _eqc.asStateFlow()

    private val _loed = MutableStateFlow<List<LevelOfDamageData>?>(null)
    val loed = _loed.asStateFlow()

    private val _techStatus = MutableStateFlow<List<techStatusData>?>(null)
    val techStatus = _techStatus.asStateFlow()

    init {
        LoadData()
    }
    fun LoadData() {
        viewModelScope.launch {
            _building.value = fetchBuildingData()
            _department.value = fetchDepartmentData()
            _eq.value = fetchEquipmentData()
            _eqc.value = fetchEquipmentTypeData()
            _loed.value = fetchLevelOfDamageData()
            _techStatus.value = fetchTectStatusData()
        }
    }
    //=================================================Add Data============================================================//
    fun addBuilding(building_room_number:String,building_floor:String,building_name:String){
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.addBuilding(
                BuildingRequest(building_room_number,building_floor.toInt(),building_name)
            )
            if(response.isSuccessful){
                Toast.makeText(getApplication(),"${response.body()}",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(getApplication(),"${response.errorBody()?.string()}",Toast.LENGTH_SHORT).show()
                Log.e(TAG, "addBuilding: ${response.errorBody()?.string()}", )
            }
        }
    }
    fun addDepartment(departmentName:String){
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.addDepartment(
                DepartmentRequest(departmentName)
            )
            if(response.isSuccessful){
                Toast.makeText(getApplication(),"${response.body()}",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(getApplication(),"${response.errorBody()?.string()}",Toast.LENGTH_SHORT).show()
                Log.e(TAG, "addDepartment: ${response.errorBody()?.string()}", )
            }
        }
    }

    fun addEquipment(eq_name:String,eq_status:String,eq_unit:String,eqc_id:String){
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.addEquipment(
                EquipmentRequest(eq_name,eq_status,eq_unit,eqc_id.toInt())
            )
            if(response.isSuccessful){
                Toast.makeText(getApplication(),"${response.body()}",Toast.LENGTH_SHORT).show()
                Log.d(TAG, "addEquipment: ${response.body().toString()}")
            }else{
                Toast.makeText(getApplication(),"${response.errorBody()?.string()}",Toast.LENGTH_SHORT).show()
                Log.e(TAG, "addEquipment: ${response.errorBody()?.string()}", )
            }
        }
    }
    fun addEquipmentType(eqc_name:String){
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.addEquipmentType(
                EquipmentTypeRequest(eqc_name)
            )
            if(response.isSuccessful){
                Toast.makeText(getApplication(),"${response.body()}",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(getApplication(),"${response.errorBody()?.string()}",Toast.LENGTH_SHORT).show()
                Log.e(TAG, "addEquipmentType: ${response.errorBody()?.string()}", )
            }
        }
    }

    fun addLevelOfDamage(loed_name:String){
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.addLevelOfDamage(
                LevelOfDamageRequest(loed_name)
            )
            if(response.isSuccessful){
                Toast.makeText(getApplication(),"${response.body()}",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(getApplication(),"${response.errorBody()?.string()}",Toast.LENGTH_SHORT).show()
                Log.e(TAG, "addLevelOfDamage: ${response.errorBody()?.string()}", )
            }
        }
    }
    fun addTechStatus(receive_request_status:String){
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.addTechStatus(
                techStatusRequest(receive_request_status)
            )
            if(response.isSuccessful){
                Toast.makeText(getApplication(),"${response.body()}",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(getApplication(),"${response.errorBody()?.string()}",Toast.LENGTH_SHORT).show()
                Log.e(TAG, "addLevelOfDamage: ${response.errorBody()?.string()}", )
            }
        }
    }
    //=================================================Edit Data============================================================//

    fun editBuilding(building_id: Int, building_room_number: String, building_floor: String, building_name: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.editBuilding(
                building_id, BuildingRequest(building_room_number, building_floor.toInt(), building_name)
            )
            if (response.isSuccessful) {
                Toast.makeText(getApplication(), "${response.body()}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(getApplication(), "${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "editBuilding: ${response.errorBody()?.string()}")
            }
        }
    }

    fun editDepartment(department_id: Int, departmentName: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.editDepartment(
                department_id, DepartmentRequest(departmentName)
            )
            if (response.isSuccessful) {
                Toast.makeText(getApplication(), "${response.body()}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(getApplication(), "${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "editDepartment: ${response.errorBody()?.string()}")
            }
        }
    }

    fun editEquipment(eq_id: Int, eq_name: String, eq_status: String, eq_unit: String, eqc_id: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.editEquipment(
                eq_id, EquipmentRequest(eq_name, eq_status, eq_unit, eqc_id.toInt())
            )
            if (response.isSuccessful) {
                Toast.makeText(getApplication(), "${response.body()}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(getApplication(), "${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "editEquipment: ${response.errorBody()?.string()}")
            }
        }
    }

    fun editEquipmentType(eqc_id: Int, eqc_name: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.editEquipmentType(
                eqc_id, EquipmentTypeRequest(eqc_name)
            )
            if (response.isSuccessful) {
                Toast.makeText(getApplication(), "${response.body()}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(getApplication(), "${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "editEquipmentType: ${response.errorBody()?.string()}")
            }
        }
    }

    fun editLevelOfDamage(loed_id: Int, loed_name: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.editLevelOfDamage(
                loed_id, LevelOfDamageRequest(loed_name)
            )
            if (response.isSuccessful) {
                Toast.makeText(getApplication(), "${response.body()}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(getApplication(), "${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "editLevelOfDamage: ${response.errorBody()?.string()}")
            }
        }
    }

    fun editTechStatus(statusId: Int, receive_request_status: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.apiService.editTechStatus(
                statusId, techStatusRequest(receive_request_status)
            )
            if (response.isSuccessful) {
                Toast.makeText(getApplication(), "${response.body()}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(getApplication(), "${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "editLevelOfDamage: ${response.errorBody()?.string()}")
            }
        }
    }
    //=================================================Delete Data============================================================//
    fun deleteData(DataType: String, DataId: Int) {
        viewModelScope.launch {
            val response = RetrofitInstance.apiService
            try {
                val result = when (DataType) {
                    "Building" -> response.deleteBuildingById(DataId)
                    "Equipment" -> response.deleteEquipmentById(DataId)
                    "EquipmentType" -> response.deleteEquipmentTypeById(DataId)
                    "Department" -> response.deleteDepartmentById(DataId)
                    "LevelOfDamage" -> response.deleteLevelOfDamageById(DataId)
                    "TechStatus" -> response.deleteTechStatus(DataId)
                    else -> throw IllegalArgumentException("Unknown user type: $DataType")
                }
                if (result.isSuccessful) {
                    result.body()?.let {
                        Log.d(
                            TAG,
                            "deleteUser: Successfully deleted $DataType with ID $DataId"
                        )
                        Toast.makeText(getApplication(), "Successfully deleted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = result.errorBody()?.string()
                    Log.e(
                        TAG,
                        "deleteUser: Failed to delete $DataType with ID $DataId - $errorBody"
                    )
                    Toast.makeText(
                        getApplication(),
                        "Failed to delete: $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "deleteUser: Exception while deleting $DataType with ID $DataId", e)
                Toast.makeText(getApplication(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


        //=================================================Fetch Data============================================================//

        suspend fun getEquipmentTypeName(typeId: Int): String {
            val response =
                RetrofitInstance.apiService.getEquipmentTypeById(typeId)
            Log.d(TAG, "getEquipmentTypeName: ${response.body()?.eqc_name}")
            return response.body()?.eqc_name ?: "null"
        }

        suspend fun fetchTectStatusData():List<techStatusData>?{
            return fetchData { RetrofitInstance.apiService.getTechStatus() }
        }
        suspend fun fetchBuildingData(): List<BuildingData>? {
            return fetchData { RetrofitInstance.apiService.getBuildings() }
        }

        suspend fun fetchDepartmentData(): List<DepartmentData>? {
            return fetchData { RetrofitInstance.apiService.getDepartments() }
        }

        suspend fun fetchEquipmentData(): List<EquipmentData>? {
            return fetchData { RetrofitInstance.apiService.getEquipments() }
        }

        suspend fun fetchEquipmentTypeData(): List<EquipmentTypeData>? {
            return fetchData { RetrofitInstance.apiService.getEquipmentTypes() }
        }

        suspend fun fetchLevelOfDamageData(): List<LevelOfDamageData>? {
            return fetchData { RetrofitInstance.apiService.getLevelOfDamages() }
        }

        private suspend fun <T> fetchData(fetchCall: suspend () -> Response<T>): T? {
            val response = fetchCall()
            if (response.isSuccessful) {
                return response.body()
            } else {
                throw Exception("Failed to fetch data: ${response.errorBody()?.string()}")
            }
        }
    }
