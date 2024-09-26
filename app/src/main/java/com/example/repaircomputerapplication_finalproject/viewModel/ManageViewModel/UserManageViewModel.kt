package com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.AdminData
import com.example.repaircomputerapplication_finalproject.model.ChiefData
import com.example.repaircomputerapplication_finalproject.model.DepartmentData
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.TechnicianBody
import com.example.repaircomputerapplication_finalproject.model.TechnicianData
import com.example.repaircomputerapplication_finalproject.model.UserModel
import com.example.repaircomputerapplication_finalproject.model.techStatusData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class UserManageViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val _admin = MutableStateFlow<List<AdminData>?>(null)
    val admin = _admin.asStateFlow()

    private val _tech = MutableStateFlow<List<TechnicianData>?>(null)
    val tech = _tech.asStateFlow()

    private val _emp = MutableStateFlow<List<EmployeeData>?>(null)
    val emp = _emp.asStateFlow()

    private val _chief = MutableStateFlow<List<ChiefData>?>(null)
    val chief = _chief.asStateFlow()

    private val _department = MutableStateFlow<List<DepartmentData>?>(null)
    val department = _department.asStateFlow()

    private val _techStatus = MutableStateFlow<List<techStatusData>?>(null)
    val techStatus = _techStatus.asStateFlow()

    // State for showing the alert dialog
    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> get() = _showErrorDialog

    // State for the error message
    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun resetErrorDialog() {
        _showErrorDialog.value = false
        _errorMessage.value = ""
    }

    init {
        loadData()
    }
    fun loadData() {
        viewModelScope.launch {
            _department.value = fetchDepartmentData()
            _techStatus.value = fetchTechStatusData()
            _admin.value = fetchAdminData()
            _tech.value = fetchTechnicianData()
            _emp.value = fetchEmployeeData()
            _chief.value = fetchChiefData()
            Log.d(TAG, "loadData: ${_department.value}")
            }
        }


    fun addUser(
        userType: String,
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        phone: String,
        department: String,
        status: String
    ) {
        viewModelScope.launch {
            val response = RetrofitInstance.apiService
            val userModel = UserModel(
                firstname,
                lastname,
                email,
                password,
                phone,
                department.toInt(),
            )
            val techModel = TechnicianBody(
                firstname,
                lastname,
                email,
                password,
                phone,
                department.toInt(),
                status.toInt()
            )
            try {
                val result = when (userType) {
                    "Admin" -> response.addAdmin(userModel)
                    "Technician" -> response.addTechnician(techModel)
                    "Employee" -> response.addEmployee(userModel)
                    "Chief" -> response.addChief(userModel)
                    else -> throw IllegalArgumentException("Unknown user type: $userType")
                }
                if (result.isSuccessful) {
                    Log.d(TAG, "addUser: Success Response: ${result.body()}")
                } else {
                    Toast.makeText(getApplication(), "${result.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    Log.d(TAG, "addUser: Error Response: ${result.errorBody()?.string()}")
                    val errorResponse = result.errorBody()?.string()
                    _errorMessage.value = errorResponse ?: "เกิดข้อผิดพลาด ไม่สามารถดำเนินการได้"
                    _showErrorDialog.value = true
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_SHORT).show()
                Log.e("UserManageViewModel", "Error adding user: $e")
            }
        }
    }
    fun editUser(
        userType: String,
        userId: String,
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        phone: String,
        department: String,
        status: String
    ) {
        viewModelScope.launch {
            val response = RetrofitInstance.apiService
            val userModel = UserModel(
                firstname,
                lastname,
                email,
                password,
                phone,
                department.toInt()
            )
            val techModel = TechnicianBody(
                firstname,
                lastname,
                email,
                password,
                phone,
                department.toInt(),
                status.toInt()
            )
            try {
                val result = when (userType) {
                    "Admin" -> response.editAdmin(userId.toInt(), userModel)
                    "Technician" -> response.editTechnician(userId.toInt(), techModel)
                    "Employee" -> response.editEmployee(userId.toInt(), userModel)
                    "Chief" -> response.editChief(userId.toInt(), userModel)
                    else -> throw IllegalArgumentException("Unknown user type: $userType")
                }
                Log.d(TAG, "editUser: Request Data: ${userModel}")
                if (result.isSuccessful) {
                    Log.d(TAG, "editUser: Success Response: ${result.body()}")
                } else {
                    Log.d(TAG, "editUser: Error Response: ${result.errorBody()?.string()}")
                    val errorResponse = result.errorBody()?.string()
                    _errorMessage.value = errorResponse ?: "เกิดข้อผิดพลาด ไม่สามารถดำเนินการได้"
                    _showErrorDialog.value = true
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_SHORT).show()
                Log.e("UserManageViewModel", "Error editing user: $e")
            }
        }
    }
    // delete data
    fun deleteUser(userType: String, userId: Int) {
        viewModelScope.launch {
            val response = RetrofitInstance.apiService
            try {
                val result = when (userType) {
                    "Admin" -> response.deleteAdminById(userId)
                    "Technician" -> response.deleteTechnicianById(userId)
                    "Employee" -> response.deleteEmployeeById(userId)
                    "Chief" -> response.deleteChiefById(userId)
                    else -> throw IllegalArgumentException("Unknown user type: $userType")
                }
                if (result.isSuccessful) {
                    result.body()?.let {
                        Log.d(TAG, "deleteUser: Successfully deleted $userType with ID $userId - ${it.message}")
                        Toast.makeText(getApplication(), "ลบข้อมูลเสร็จสิ้น", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = result.errorBody()?.string()
                    Log.e(TAG, "deleteUser: Failed to delete $userType with ID $userId - $errorBody")
                    Toast.makeText(getApplication(), "$errorBody", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "deleteUser: Exception while deleting $userType with ID $userId", e)
                Log.e(TAG, "Error: ${e.message}", e)
                Toast.makeText(getApplication(), "เกิดข้อผิดพลาดไม่สามารถนำเนินการได้", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun getTechStatusName(statusId: Int): String {
        val response = RetrofitInstance.apiService.getTechStatusById(statusId)
        return response.body()?.receive_request_status ?: "null"
    }
    suspend fun getDepartmentName(departmentId:Int):String{
        val response = RetrofitInstance.apiService.getDepartmentById(departmentId)
        return response.body()?.departmentName ?: "null"
    }
    private suspend fun fetchEmployeeData(): List<EmployeeData>? {
        return fetchData { RetrofitInstance.apiService.getEmployees() }
    }

    private suspend fun fetchAdminData(): List<AdminData>? {
        return fetchData { RetrofitInstance.apiService.getAdmins() }
    }

    private suspend fun fetchTechnicianData(): List<TechnicianData>? {
        return fetchData { RetrofitInstance.apiService.getTechnicians() }
    }

    private suspend fun fetchChiefData(): List<ChiefData>? {
        return fetchData { RetrofitInstance.apiService.getChiefs() }
    }
    private suspend fun fetchDepartmentData():List<DepartmentData>?{
        return fetchData { RetrofitInstance.apiService.getDepartments() }
    }
    private suspend fun fetchTechStatusData():List<techStatusData>? {
        return fetchData { RetrofitInstance.apiService.getTechStatus() }
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