package com.example.repaircomputerapplication_finalproject.model


data class  UserRequest(
    val email: String,
    val password: String
)
data class UserResponse(val message:String? = null, val userSession: UserSession,val user : UserModel,val sessionId: String?)
data class logoutResponse(val message: String?)
data class UserSession(
    val userId:Int = 0,
    val user:String = "",
    val role:String = "",
    val IsLogin:Boolean = false
)
data class UserModel(
    private val firstname: String? = null,
    private val lastname: String? = null,
    private val email: String? = null,
    private val password: String? = null,
    private val phone: String? = null,
    private val departmentId:Int? = 0,
    private val status_id:Int? = null,
)
data class TechnicianBody(
    private val firstname: String? = null,
    private val lastname: String? = null,
    private val email: String? = null,
    private val password: String? = null,
    private val phone: String? = null,
    private val departmentId:Int? = 0,
    private val status_id:Int? = null,
)
data class AdminData(
    val admin_id:Int? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val password: String? = null,
    val departmentId:Int? = 0,
    val status_id:Int? = null,
)
data class EmployeeData(
    val emp_id:Int? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val password: String? = null,
    val departmentId:Int? = 0,
)
data class TechnicianData(
    val tech_id:Int? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val password: String? = null,
    val departmentId:Int? = 0,
    val status_id:Int? = null,
)
data class ChiefData(
    val chief_id:Int? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val password: String? = null,
    val departmentId:Int? = 0,
)