package com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class DataManageViewModel(application: Application): AndroidViewModel(application) {

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
    fun addBuilding(building_room_number: String, building_floor: String, building_name: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.addBuilding(
                    BuildingRequest(building_room_number, building_floor.toInt(), building_name)
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully added", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "addBuilding: Success Response: ${response.body()}")
                } else {
                    Toast.makeText(getApplication(), "Failed to add: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "addBuilding: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error adding building: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error adding building: $e")
            }
        }
    }

    fun addDepartment(departmentName: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.addDepartment(
                    DepartmentRequest(departmentName)
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully added", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "addDepartment: Success Response: ${response.body()}")
                } else {
                    Toast.makeText(getApplication(), "Failed to add: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "addDepartment: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error adding department: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error adding department: $e")
            }
        }
    }

    fun addEquipment(eq_name: String, eq_status: String, eq_unit: String, eqc_id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.addEquipment(
                    EquipmentRequest(eq_name, eq_status, eq_unit, eqc_id.toInt())
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully added", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "addEquipment: Success Response: ${response.body()}")
                } else {
                    Toast.makeText(getApplication(), "Failed to add: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "addEquipment: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error adding equipment: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error adding equipment: $e")
            }
        }
    }

    fun addEquipmentType(eqc_name: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.addEquipmentType(
                    EquipmentTypeRequest(eqc_name)
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully added", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "addEquipmentType: Success Response: ${response.body()}")
                } else {
                    Toast.makeText(getApplication(), "Failed to add: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "addEquipmentType: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error adding equipment type: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error adding equipment type: $e")
            }
        }
    }

    fun addLevelOfDamage(loed_name: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.addLevelOfDamage(
                    LevelOfDamageRequest(loed_name)
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully added", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "addLevelOfDamage: Success Response: ${response.body()}")
                } else {
                    Toast.makeText(getApplication(), "Failed to add: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "addLevelOfDamage: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error adding level of damage: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error adding level of damage: $e")
            }
        }
    }

    fun addTechStatus(receive_request_status: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.addTechStatus(
                    techStatusRequest(receive_request_status)
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully added", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "addTechStatus: Success Response: ${response.body()}")
                } else {
                    Toast.makeText(getApplication(), "Failed to add: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "addTechStatus: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error adding tech status: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error adding tech status: $e")
            }
        }
    }

    //=================================================Edit Data============================================================//
    fun editBuilding(building_id: Int, building_room_number: String, building_floor: String, building_name: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.editBuilding(
                    building_id, BuildingRequest(building_room_number, building_floor.toInt(), building_name)
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully edited", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getApplication(), "Failed to edit: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "editBuilding: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error editing building: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error editing building: $e")
            }
        }
    }

    fun editDepartment(department_id: Int, departmentName: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.editDepartment(
                    department_id, DepartmentRequest(departmentName)
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully edited", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getApplication(), "Failed to edit: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "editDepartment: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error editing department: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error editing department: $e")
            }
        }
    }

    fun editEquipment(eq_id: Int, eq_name: String, eq_status: String, eq_unit: String, eqc_id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.editEquipment(
                    eq_id, EquipmentRequest(eq_name, eq_status, eq_unit, eqc_id.toInt())
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully edited", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getApplication(), "Failed to edit: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "editEquipment: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error editing equipment: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error editing equipment: $e")
            }
        }
    }

    fun editEquipmentType(eqc_id: Int, eqc_name: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.editEquipmentType(
                    eqc_id, EquipmentTypeRequest(eqc_name)
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully edited", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getApplication(), "Failed to edit: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "editEquipmentType: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error editing equipment type: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error editing equipment type: $e")
            }
        }
    }

    fun editLevelOfDamage(loed_id: Int, loed_name: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.editLevelOfDamage(
                    loed_id, LevelOfDamageRequest(loed_name)
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully edited", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getApplication(), "Failed to edit: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "editLevelOfDamage: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error editing level of damage: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error editing level of damage: $e")
            }
        }
    }

    fun editTechStatus(statusId: Int, receive_request_status: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.editTechStatus(
                    statusId, techStatusRequest(receive_request_status)
                )
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "Successfully edited", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getApplication(), "Failed to edit: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "editTechStatus: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error editing tech status: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error editing tech status: $e")
            }
        }
    }

    //=================================================Delete Data============================================================//
    fun deleteData(DataType: String, DataId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService
                val result = when (DataType) {
                    "Building" -> response.deleteBuildingById(DataId)
                    "Equipment" -> response.deleteEquipmentById(DataId)
                    "EquipmentType" -> response.deleteEquipmentTypeById(DataId)
                    "Department" -> response.deleteDepartmentById(DataId)
                    "LevelOfDamage" -> response.deleteLevelOfDamageById(DataId)
                    "TechStatus" -> response.deleteTechStatus(DataId)
                    else -> throw IllegalArgumentException("Unknown data type: $DataType")
                }
                if (result.isSuccessful) {
                    when (DataType) {
                        "Building" -> _building.value = _building.value?.filterNot { it.building_id == DataId }
                        "Equipment" -> _eq.value = _eq.value?.filterNot { it.eq_id == DataId }
                        "EquipmentType" -> _eqc.value = _eqc.value?.filterNot { it.eqc_id == DataId }
                        "Department" -> _department.value = _department.value?.filterNot { it.department_id == DataId }
                        "LevelOfDamage" -> _loed.value = _loed.value?.filterNot { it.loed_id == DataId }
                        "TechStatus" -> _techStatus.value = _techStatus.value?.filterNot { it.status_id == DataId }
                    }
                    Toast.makeText(getApplication(), "ลบข้อมูลเสร็จสิ้น", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "deleteData: Successfully deleted $DataType with ID $DataId")
                } else {
                    val errorBody = result.errorBody()?.string()
                    Toast.makeText(getApplication(), "$errorBody", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "deleteData: Failed to delete $DataType with ID $DataId - $errorBody")
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Error deleting data: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "deleteData: Exception while deleting $DataType with ID $DataId", e)
            }
        }
    }

    //=================================================Fetch Data============================================================//

    suspend fun getEquipmentTypeName(typeId: Int): String {
        val response = RetrofitInstance.apiService.getEquipmentTypeById(typeId)
        Log.d(TAG, "getEquipmentTypeName: ${response.body()?.eqc_name}")
        return response.body()?.eqc_name ?: "null"
    }

    suspend fun fetchTectStatusData(): List<techStatusData>? {
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
        return if (response.isSuccessful) {
            response.body()
        } else {
            throw Exception("Failed to fetch data: ${response.errorBody()?.string()}")
        }
    }
}
