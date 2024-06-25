package com.example.repaircomputerapplication_finalproject.model

// data/Models.kt
data class RequestForRepair(
    val rrid: Int,
    val rrDescription: String,
    val rrPicture: String,
    val requestStatus: String,
    val timestamp: String
)

data class RepairDetails(
    val rdId: Int,
    val loedId: Int,
    val rcceId: Int,
    val rdDescription: String,
    val timestamp: String
)

data class ReceiveRepair(
    val rcceId: Int,
    val techId: Int,
    val dateReceive: String,
    val rrid: Int
)

data class RepairStatus(
    val timestamp: String,
    val status: String,
    val subStatus: List<String> = emptyList()
)
