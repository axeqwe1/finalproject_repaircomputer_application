package com.example.repaircomputerapplication_finalproject.model


data class RepairReport(
    val equipmentName: String,
    val repairCount: Int,
    val equipmentType: String,
    val technicianName: String,
    val totalRequests: Int
)

data class DashboardModel(
    val TotalWork:Int? = null,
    val ReceiveWork:Int? = null,
    val Backlog:Int? = null,
    val SuccessWork:Int? = null,
)