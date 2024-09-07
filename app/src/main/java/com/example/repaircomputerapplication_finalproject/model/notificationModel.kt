package com.example.repaircomputerapplication_finalproject.model


data class notificationListResponse(val data:List<notificationData>)
data class notificationData(
    val noti_id:String? = "",
    val noti_message:String? = "",
    val admin_id:Int? = null,
    val emp_id:Int? = null,
    val tech_id:Int? = null,
    val timestamp:String? = null,
    val isRead:Boolean? = null
)

data class NotificationMessage(
    val title: String,
    val message: String,
    val user_id: String,
    val role: String,
    val timestamp: String
)

data class NotificationReadRequest(
    val userId: String,
    val role: String
)