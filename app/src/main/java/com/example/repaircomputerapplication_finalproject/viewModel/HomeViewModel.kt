package com.example.repaircomputerapplication_finalproject.viewModel

import SessionManager
import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.AdminData
import com.example.repaircomputerapplication_finalproject.model.ChiefData
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.NotificationReadRequest
import com.example.repaircomputerapplication_finalproject.model.TechnicianData
import com.example.repaircomputerapplication_finalproject.repository.NotificationRepository
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val dataStore = application.dataStore
    private val _logoutResult = MutableStateFlow<LogoutResult?>(null)
    private val _notificationCount = MutableStateFlow(0)
    val notificationCount: StateFlow<Int> = NotificationRepository.notificationCount
    val logoutResult: StateFlow<LogoutResult?> = _logoutResult
    val sessionManager:SessionManager = SessionManager(application)


    // SharedFlow สำหรับรับข้อมูลจาก WebSocket
    private val _notificationsFlow = MutableSharedFlow<Int>()
    val notificationsFlow: SharedFlow<Int> = _notificationsFlow


    init {
        // สังเกตการเปลี่ยนแปลงของ _notificationsFlow และอัปเดต notificationCount
        viewModelScope.launch {
            _notificationsFlow.collect { count ->
                _notificationCount.value = count
            }
        }
    }
    fun logout(){
        viewModelScope.launch{
            val response = RetrofitInstance.apiService.userLogout()
            try {
                if(response.isSuccessful){
                    removeUserId()
                    sessionManager.clearSession()
                    _logoutResult.value = LogoutResult.IsLogout
                }
            } catch (e: Exception) {
                    _logoutResult.value = LogoutResult.Failure(e.message ?: "Unknown error occurred")
            }
        }
    }

    // ฟังก์ชันสำหรับดึงข้อมูลแจ้งเตือนใหม่จากเซิร์ฟเวอร์
    fun fetchNotifications() {
        viewModelScope.launch {
            try {
                val id = dataStore.data.map { item ->
                    item[stringPreferencesKey("userId")]
                }.first()
                val role = dataStore.data.map { item ->
                    item[stringPreferencesKey("role")]
                }.first()

                val response = RetrofitInstance.apiService.getNotification(id = id!!.toInt(), role = role!!.toString())
                if (response.isSuccessful) {
                    val notifications = response.body()?.data ?: emptyList()
                    val unreadNotifications = notifications.filter { it.isRead == false }
                    updateNotificationCount(unreadNotifications.size) // กำหนดจำนวนแจ้งเตือนใหม่
                } else {
                    throw Exception("Failed to fetch notifications: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching notifications: ${e.message}")
            }
        }
    }

    // ฟังก์ชันสำหรับอัปเดตจำนวนแจ้งเตือน
    fun updateNotificationCount(count: Int) {
        NotificationRepository.setNotificationCount(count)
    }

    // รีเซ็ตจำนวนแจ้งเตือนเมื่อเข้าไปดูในหน้า "แจ้งเตือน"
    fun resetNotificationCount() {
        markAsReadNotification()
        NotificationRepository.resetNotificationCount()
    }

    private fun markAsReadNotification(){
        viewModelScope.launch {
            try {
                val id = dataStore.data.map { item ->
                    item[stringPreferencesKey("userId")]
                }.first()
                val role= dataStore.data.map { item ->
                    item[stringPreferencesKey("role")]
                }.first()
                val response = RetrofitInstance.apiService.markAsReadNotification(
                    NotificationReadRequest(id!!,role!!)
                )
                if(response.isSuccessful){
                    Log.d(TAG, "markAsReadNotification: ${response.body()}")
                }else{
                    throw Exception("Fail to Mark As Read")
                }
            }catch (e:Exception){
                Log.d(TAG, "markAsReadNotification: $e")
            }
        }
    }
    private suspend fun removeUserId(){
        val userIdKey = stringPreferencesKey("userId")
        val roleKey = stringPreferencesKey("role")
        val isLoginKey = booleanPreferencesKey("isLogin")
        dataStore.edit { settings ->
            settings[userIdKey] = ""
            settings[roleKey] = ""
            settings[isLoginKey] = false
        }
    }

    suspend fun getTechnicianData(userId:Int):TechnicianData?{
        try {
            val response = RetrofitInstance.apiService.getTechnicianById(userId)
            if(response.isSuccessful){
                Log.d(TAG, "getTechnicianData: ${response.body()}")
                return response.body()
            }else{
                throw Exception("fail to get Employee Name")
            }
        }catch (e:Exception){
            throw Exception("getTechnicianData:$e")
        }

    }
    suspend fun getAdminData(userId:Int):AdminData?{
        try {
            val response = RetrofitInstance.apiService.getAdminById(userId)
            if(response.isSuccessful){
                Log.d(TAG, "getAdminData: ${response.body()}")
                return response.body()
            }else{
                throw Exception("fail to get AdminData")
            }
        }catch (e:Exception){
            throw Exception("getAdminData:$e")
        }
    }
//
    suspend fun getEmployeeData(userId:Int):EmployeeData?{
        try {
            val response = RetrofitInstance.apiService.getEmployeeById(userId)
            if(response.isSuccessful){
                Log.d(TAG, "getEmployeeData: ${response.body()}")
                return response.body()
            }else{
                throw Exception("fail to get EmployeeData")
            }
        }catch (e:Exception){
            throw Exception("getEmployeeData:$e")
        }

    }
//    suspend fun getChiefData(userId:Int): ChiefData?{
//        try {
//            val response = RetrofitInstance.apiService.getChiefById(userId)
//            if(response.isSuccessful){
//                Log.d(TAG, "getChiefData: ${response.body()}")
//                return response.body()
//            }else{
//                throw Exception("fail to get ChiefData")
//            }
//        }catch (e:Exception){
//            throw Exception("getChiefData:$e")
//        }
//    }
}


//fun restoreSession(context: Context, apiService: ApiService) {
//    if (getSessionToken(context) != null) {
//        viewModelScope.launch {
//            try {
//                val response = apiService.getUserData()  // API call to get user data
//                if (response.isSuccessful) {
//                    // Update user data in local storage or in-memory storage
//                    updateUserSession(response.body())
//                } else {
//                    // Handle session expiration or invalid session
//                    clearSession(context)
//                }
//            } catch (e: Exception) {
//                // Handle errors appropriately
//            }
//        }
//    }
//}
sealed class LogoutResult {
    object IsLogout : LogoutResult()
    data class Failure(val error: String) : LogoutResult()
}