package com.example.repaircomputerapplication_finalproject.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object NotificationRepository {
    private val _notificationCount = MutableStateFlow(0)
    val notificationCount: StateFlow<Int> = _notificationCount

    fun incrementNotificationCount() {
        _notificationCount.value += 1
    }

    fun resetNotificationCount() {
        _notificationCount.value = 0
    }

    fun setNotificationCount(count:Int){
        _notificationCount.value = count
    }
}