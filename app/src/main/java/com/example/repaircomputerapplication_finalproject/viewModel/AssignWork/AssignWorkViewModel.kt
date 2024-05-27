package com.example.repaircomputerapplication_finalproject.viewModel.AssignWork

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.`api-service`.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.BacklogResponse
import com.example.repaircomputerapplication_finalproject.model.DepartmentData
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.detailRepairData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AssignWorkViewModel(application: Application):AndroidViewModel(application){

    private var _backlogData = MutableStateFlow<BacklogResponse?>(null)
    val backlogData = _backlogData.asStateFlow()

    private var _departmentData = MutableStateFlow<List<DepartmentData>?>(null)
    val departmentData = _departmentData.asStateFlow()

    init {
        loadData()
    }

    fun loadData(){
        viewModelScope.launch {
            _backlogData.value = fetchRequestListNotReceive()
            _departmentData.value = fetchDepartmentData()
        }
    }
    fun getDepartmentName(depart_Id: Int):String{
        departmentData.value?.forEach{items ->
            if(items.department_id == depart_Id){
                return items.departmentName
            }
        }
        return "Fail to getDepartmentName"
    }
    suspend fun fetchDepartmentData() : List<DepartmentData>?{
        val response = RetrofitInstance(getApplication()).apiService.getDepartments()
        if(response.isSuccessful){
            return response.body()
        }else{
            Toast.makeText(getApplication(),"QueryData  Fail", Toast.LENGTH_SHORT).show()
            throw Exception("Fail to Fetch department Data")
        }
    }
    suspend fun fetchRequestListNotReceive():BacklogResponse{
        val response = RetrofitInstance(getApplication()).apiService.getBackLogRequest()
        if (response.isSuccessful && response.body() != null){
            return response.body()!!
        }else{
            throw Exception("Fail to Fetch BacklogData")
        }
    }
}