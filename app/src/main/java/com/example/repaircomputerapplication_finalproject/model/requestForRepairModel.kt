package com.example.repaircomputerapplication_finalproject.model



data class RequestForRepairData(
    val rrid: Int?,
    val rr_description: String,
    val rr_picture: String,
    val request_status: String,
    val timestamp: String,
    val employee_id: Int?,
    val building_id: Int?,
    val eq_id: Int?,
    val receive_repair: ReceiveRepairModel,
    val assign_work: AssignWorkModel
)
data class RequestListResponse(val message:String?,val data:List<detailRepairData>)
data class UploadResponse(
    val message: String,
    val filename: String? = null  // 'path' instead of 'imageUrl'
)
data class sendRequest (
    val description: String = "",
    val picture: String = "",
    val employeeId:Int? = null,
    val buildingId:Int? = null,
    val equipmentId:Int? =  null,
)

data class RequestResponse (val repair:RequestForRepairData)

data class BacklogResponse(val data:List<backlogData>)

data class TechBacklogCount(
    val Backlog : Int? = null,
    val SuccessWork : Int? = null,
    val TotalWork : Int? = null,
)
data class backlogData(
    val rrid: Int?,
    val rr_description: String,
    val rr_picture: String,
    val request_status: String,
    val timestamp: String,
    val employee_id: Int?,
    val building_id: Int?,
    val eq_id: Int?,
    val equipment:EquipmentForDetail,
    val employee: EmployeeData,
    val building:BuildingData,
    val assign_work: AssignWorkModel
)