package com.example.repaircomputerapplication_finalproject.model

data class DepartmentData(
    val department_id:Int? = null,
    val departmentName:String = ""
)

data class DepartmentRequest(
    val departmentName:String = ""
)