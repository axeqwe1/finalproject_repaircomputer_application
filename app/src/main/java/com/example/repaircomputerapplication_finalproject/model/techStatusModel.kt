package com.example.repaircomputerapplication_finalproject.model

data class techStatusData(
    val status_id:Int? = null,
    val receive_request_status:String? = ""
)

data class techStatusRequest(
    val receive_request_status:String? = ""
)