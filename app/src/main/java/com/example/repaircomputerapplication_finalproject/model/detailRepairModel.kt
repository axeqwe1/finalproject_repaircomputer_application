package com.example.repaircomputerapplication_finalproject.model

data class detailRepairData(
    val rrid: Int?,
    val rr_description: String,
    val rr_picture: String,
    val request_status: String,
    val timestamp: String,
    val employee_id: Int?,
    val building_id: Int?,
    val eq_id: Int?,
    val receive_repair: ReceiveRepairDataForDetail,
    val equipment:EquipmentForDetail,
    val employee: EmployeeData,
    val building:BuildingData,
    val assign_work: AssignWorkModel
)
data class ReceiveRepairDataForDetail(
    val rrce_id: Int? = null,
    val tech_id: Int? = null,
    val date_receive: String? = null,
    val rrid: Int? = null,
    val repair_details:List<RepairDetailForDetail>,
    val technician : TechnicianData,

)

data class RepairDetailForDetail(
    val rd_id:Int? = null,
    val loed_id:Int? = null,
    val rrce_id: Int? = null,
    val rd_description:String? = "",
    val timestamp: String? = "",
    val levelOfDamage:LevelOfDamageData? = null
)
data class EquipmentForDetail(
    val eq_id:Int? = null,
    val eq_name:String? = null,
    val eq_status:String? = null,
    val eq_unit:String? = null,
    val eq_start_date:String,
    val eqc_id:Int? = null,
    val equipment_Type:EquipmentTypeData? = null
)

data class addDetailRequest(
    val loed_id:Int,
    val rrce_id:Int,
    val rd_description:String,
    val request_status: String
)

data class updateDetailRequest(
    val loed_id:Int,
    val rd_description:String,
    val request_status:String
)