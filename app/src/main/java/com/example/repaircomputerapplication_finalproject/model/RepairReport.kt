package com.example.repaircomputerapplication_finalproject.model


data class RepairReport(
    val rrid: Int,
    val eq_id: Int,
    val request_status: String,
    val eq_name: String,
    val eqc_name: String,
    val firstname: String? = null,
    val lastname: String? = null,
    val repair_count: Int,
    val timestamp: String,
    val totalRequests: Int
)
data class DateForReport(
    val start_date: String,
    val end_date: String,
)
data class DashboardModel(
    val TotalWork:Int? = null,
    val ReceiveWork:Int? = null,
    val Backlog:Int? = null,
    val SuccessWork:Int? = null,
)