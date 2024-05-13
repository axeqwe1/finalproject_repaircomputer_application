package com.example.repaircomputerapplication_finalproject.viewModel

import SessionManager
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.`api-service`.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.UserRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.Exception


class AuthViewModel(application: Application): AndroidViewModel(application) {
    private val dataStore = application.dataStore
    private val _loginResult = MutableStateFlow<Boolean?>(null)
    val loginResult: StateFlow<Boolean?> = _loginResult
    private val _protectedState = MutableStateFlow<String?>(null)
    val protectedState:StateFlow<String?> = _protectedState
    private val sessionManager:SessionManager = SessionManager(application)
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance(getApplication()).apiService.userLogin(UserRequest(email, password))
                if (response.isSuccessful && response.body()?.message == "Logged in successfully!") {
                    response.body()?.userSession?.let{
                        item -> saveUserId(item.userId,item.role,item.IsLogin)
                        sessionManager.saveSessionToken(response.body()?.sessionId.toString())
                        _loginResult.value = item.IsLogin
                    }
                    Log.d(TAG, response.body()!!.toString())

                } else {
                    Log.d(TAG, "login: ${response.body()}")
                    Toast.makeText(getApplication(), "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_SHORT).show()
                Log.d(TAG, "login: $e")
            }
        }
    }
    fun getSessionKey() = sessionManager.sessionKey

    private suspend fun saveUserId(userId: Int, role: String, isLogin: Boolean) {
        val userIdKey = stringPreferencesKey("userId")
        val roleKey = stringPreferencesKey("role")
        val isLoginKey = booleanPreferencesKey("isLogin")
        dataStore.edit { settings ->
            settings[userIdKey] = userId.toString()
            settings[roleKey] = role
            settings[isLoginKey] = isLogin
        }
    }
}



