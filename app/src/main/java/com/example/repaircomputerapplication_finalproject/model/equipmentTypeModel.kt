package com.example.repaircomputerapplication_finalproject.model

data class EquipmentTypeData (
    val eqc_id: Int? = null,
    val eqc_name: String = ""
)

data class EquipmentTypeRequest(
    val eqc_name: String = ""
)