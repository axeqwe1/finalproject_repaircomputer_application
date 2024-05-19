package com.example.repaircomputerapplication_finalproject.model

data class EquipmentData (
    val eq_id:Int? = null,
    val eq_name:String? = null,
    val eq_status:String? = null,
    val eq_unit:String? = null,
    val eq_start_date:String,
    val eqc_id:Int? = null
)

data class EquipmentRequest(
    val eq_name:String? = null,
    val eq_status:String? = null,
    val eq_unit:String? = null,
    val eqc_id:Int? = null
)