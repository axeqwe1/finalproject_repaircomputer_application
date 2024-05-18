package com.example.repaircomputerapplication_finalproject.viewModel

import SessionManager
import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.`api-service`.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val dataStore = application.dataStore
    private val _logoutResult = MutableStateFlow<LogoutResult?>(null)
    val logoutResult: StateFlow<LogoutResult?> = _logoutResult
    val sessionManager:SessionManager = SessionManager(application)

    fun logout(){
        viewModelScope.launch{
            val response = RetrofitInstance(getApplication()).apiService.userLogout()
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