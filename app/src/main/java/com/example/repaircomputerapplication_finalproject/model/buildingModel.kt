package com.example.repaircomputerapplication_finalproject.model


data class BuildingData(
    val building_id:Int? = null,
    val building_room_number:String = "",
    val building_floor:Int? = null,
    val building_name:String = ""
)

data class BuildingRequest(
    val building_room_number:String = "",
    val building_floor:Int? = null,
    val building_name:String = ""
)

