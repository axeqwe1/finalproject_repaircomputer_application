package com.example.repaircomputerapplication_finalproject.model


data class notificationListResponse(val data:List<notificationData>)
data class notificationData(
    val noti_id:String? = "",
    val noti_message:String? = "",
    val admin_id:Int? = null,
    val emp_id:Int? = null,
    val tech_id:Int? = null,
    val timestamp:String? = null
)