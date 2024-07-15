package com.example.repaircomputerapplication_finalproject.`api-service`

import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ConnectionChecker {

    suspend fun checkConnection(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.apiService.checkConnection()
                response.success
            } catch (e: Exception) {
                false
            }
        }
    }

    fun initiateConnectionCheck(scope: CoroutineScope, onResult: (Boolean) -> Unit) {
        scope.launch {
            val isConnected = checkConnection()
            onResult(isConnected)
        }
    }
}
