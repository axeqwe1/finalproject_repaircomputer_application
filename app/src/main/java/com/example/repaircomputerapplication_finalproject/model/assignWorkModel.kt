package com.example.repaircomputerapplication_finalproject.model

data class AssignWorkModel(
    val as_id: Int,
    val admin_id: Int,
    val tech_id: Int,
    val rrid: Int,
    val timestamp: String
)

data class AssignWorkBody(
    val admin_id: Int,
    val tech_id: Int,
    val rrid:Int
)