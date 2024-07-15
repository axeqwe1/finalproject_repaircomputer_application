package com.example.repaircomputerapplication_finalproject.viewModel

import android.app.Application
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.notificationData
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application):AndroidViewModel(application) {
    private var _notiList = MutableStateFlow<List<notificationData>?>(null)
    val notiList = _notiList.asStateFlow()
    val dataStore = application.dataStore

    init {
        loadData()
    }
    fun loadData(){
        viewModelScope.launch {
            _notiList.value = fetchNotiList()
        }
    }

    suspend fun fetchNotiList():List<notificationData>?{
        val id = dataStore.data.map { item ->
            item[stringPreferencesKey("userId")]
        }.first()
        val role= dataStore.data.map { item ->
            item[stringPreferencesKey("role")]
        }.first()
        val response = RetrofitInstance.apiService.getNotification(role!!,id!!.toInt())
        if(response.isSuccessful){
            return response.body()?.data
        }else{
            throw Exception("Fail To Fetch Data")
        }
    }
}