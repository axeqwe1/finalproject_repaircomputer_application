package com.example.repaircomputerapplication_finalproject.viewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.UserRequest
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application): AndroidViewModel(application) {
    private val dataStore = application.dataStore
    private val _loginResult = MutableStateFlow<Boolean?>(null)
    val loginResult: StateFlow<Boolean?> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.userLogin(UserRequest(email, password))
                if (response.isSuccessful && response.body()?.message == "Logged in successfully!") {
                    response.body()?.userSession?.let { item ->
                        saveUserId(item.userId, item.role, item.IsLogin)
                        _loginResult.value = item.IsLogin
                    }
                    Log.d(TAG, response.body()!!.toString())
                } else {
                    Log.d(TAG, "login: ${response.body()}")
                    Toast.makeText(getApplication(), "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    _loginResult.value = false
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_SHORT).show()
                Log.d(TAG, "login: $e")
                _loginResult.value = false
            }
        }
    }

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
