package com.example.repaircomputerapplication_finalproject.`api-service`

import com.example.repaircomputerapplication_finalproject.model.AdminData
import com.example.repaircomputerapplication_finalproject.model.AssignWorkBody
import com.example.repaircomputerapplication_finalproject.model.BacklogResponse
import com.example.repaircomputerapplication_finalproject.model.BuildingData
import com.example.repaircomputerapplication_finalproject.model.BuildingRequest
import com.example.repaircomputerapplication_finalproject.model.ChiefData
import com.example.repaircomputerapplication_finalproject.model.DashboardModel
import com.example.repaircomputerapplication_finalproject.model.DepartmentData
import com.example.repaircomputerapplication_finalproject.model.DepartmentRequest
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.EquipmentData
import com.example.repaircomputerapplication_finalproject.model.EquipmentRequest
import com.example.repaircomputerapplication_finalproject.model.EquipmentTypeData
import com.example.repaircomputerapplication_finalproject.model.EquipmentTypeRequest
import com.example.repaircomputerapplication_finalproject.model.LevelOfDamageData
import com.example.repaircomputerapplication_finalproject.model.LevelOfDamageRequest
import com.example.repaircomputerapplication_finalproject.model.RequestForRepairData
import com.example.repaircomputerapplication_finalproject.model.RequestListResponse
import com.example.repaircomputerapplication_finalproject.model.RequestResponse
import com.example.repaircomputerapplication_finalproject.model.TechBacklogCount
import com.example.repaircomputerapplication_finalproject.model.TechnicianBody
import com.example.repaircomputerapplication_finalproject.model.TechnicianData

import com.example.repaircomputerapplication_finalproject.model.UserRequest
import com.example.repaircomputerapplication_finalproject.model.UserResponse
import com.example.repaircomputerapplication_finalproject.model.sendRequest
import com.example.repaircomputerapplication_finalproject.model.UploadResponse
import com.example.repaircomputerapplication_finalproject.model.UserModel
import com.example.repaircomputerapplication_finalproject.model.addDetailRequest
import com.example.repaircomputerapplication_finalproject.model.detailRepairData
import com.example.repaircomputerapplication_finalproject.model.logoutResponse
import com.example.repaircomputerapplication_finalproject.model.notificationData
import com.example.repaircomputerapplication_finalproject.model.notificationListResponse
import com.example.repaircomputerapplication_finalproject.model.techStatusData
import com.example.repaircomputerapplication_finalproject.model.techStatusRequest
import com.example.repaircomputerapplication_finalproject.model.updateDetailRequest
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface IAPIService {

    //---------------------------->ManagemenData
    @GET("managementdata/getbuildings")
    suspend fun getBuildings():Response<List<BuildingData>>
    @GET("managementdata/getbuildings/{id}")
    suspend fun getBuildingById(@Path("id") buildingId: Int): Response<BuildingData>
    @GET("managementdata/getequipments")
    suspend fun getEquipments():Response<List<EquipmentData>>
    @GET("managementdata/getequipment/{id}")
    suspend fun getEquipmentById(@Path("id") eq_id: Int): Response<EquipmentData>
    @GET("managementdata/getequipmentstypes")
    suspend fun getEquipmentTypes():Response<List<EquipmentTypeData>>
    @GET("managementdata/getequipmenttype/{id}")
    suspend fun getEquipmentTypeById(@Path("id") eqc_id: Int): Response<EquipmentTypeData>
    @GET("managementdata/getdepartments")
    suspend fun getDepartments():Response<List<DepartmentData>>
    @GET("managementdata/getdepartment/{id}")
    suspend fun getDepartmentById(@Path("id") department_id: Int): Response<DepartmentData>
    @GET("managementdata/getloeds")
    suspend fun getLevelOfDamages():Response<List<LevelOfDamageData>>
    @GET("managementdata/getloed/{id}")
    suspend fun getLevelOfDamageById(@Path("id") loed: Int): Response<LevelOfDamageData>
    @GET("managementdata/gettechstatus")
    suspend fun getTechStatus():Response<List<techStatusData>>
    @GET("managementdata/gettechstatus/{id}")
    suspend fun getTechStatusById(@Path("id") status_id:Int):Response<techStatusData>
    //-----------------------------AddData
    @POST("managementdata/addbuilding")
    suspend fun addBuilding(@Body bodyrequest: BuildingRequest):Response<ResponseBody>
    @POST("managementdata/adddepartment")
    suspend fun addDepartment(@Body bodyrequest: DepartmentRequest):Response<ResponseBody>
    @POST("managementdata/addequipment")
    suspend fun addEquipment(@Body bodyrequest: EquipmentRequest):Response<ResponseBody>
    @POST("managementdata/addequipmenttype")
    suspend fun addEquipmentType(@Body bodyrequest: EquipmentTypeRequest):Response<ResponseBody>
    @POST("managementdata/addloed")
    suspend fun addLevelOfDamage(@Body bodyrequest: LevelOfDamageRequest):Response<ResponseBody>

    @POST("managementdata/addtechstatus")
    suspend fun addTechStatus(@Body bodyrequest:techStatusRequest):Response<ResponseBody>

    //-------------------------------Edit Data
    @PUT("managementdata/updatebuilding/{id}")
    suspend fun editBuilding(@Path("id") id: Int, @Body request: BuildingRequest): Response<ResponseBody>

    @PUT("managementdata/updatedepartment/{id}")
    suspend fun editDepartment(@Path("id") id: Int, @Body request: DepartmentRequest): Response<ResponseBody>

    @PUT("managementdata/updateequipment/{id}")
    suspend fun editEquipment(@Path("id") id: Int, @Body request: EquipmentRequest): Response<ResponseBody>

    @PUT("managementdata/updateequipmenttype/{id}")
    suspend fun editEquipmentType(@Path("id") id: Int, @Body request: EquipmentTypeRequest): Response<ResponseBody>

    @PUT("managementdata/updateloed/{id}")
    suspend fun editLevelOfDamage(@Path("id") id: Int, @Body request: LevelOfDamageRequest): Response<ResponseBody>

    @PUT("managementdata/updatetechstatus/{id}")
    suspend fun editTechStatus(@Path("id") id:Int,@Body techStatusRequest: techStatusRequest):Response<ResponseBody>

    //-------------------------------Delete Data
    @DELETE("managementdata/deleteequipment/{id}")
    suspend fun deleteEquipmentById(@Path("id") id: Int): Response<ResponseBody>
    @DELETE("managementdata/deleteequipmenttype/{id}")
    suspend fun deleteEquipmentTypeById(@Path("id") id: Int): Response<ResponseBody>
    @DELETE("managementdata/deleteloed/{id}")
    suspend fun deleteLevelOfDamageById(@Path("id") id: Int): Response<ResponseBody>
    @DELETE("managementdata/deletebuilding/{id}")
    suspend fun deleteBuildingById(@Path("id") id: Int): Response<ResponseBody>
    @DELETE("managementdata/deletedepartment/{id}")
    suspend fun deleteDepartmentById(@Path("id") id: Int): Response<ResponseBody>
    @DELETE("managementdata/deletetechstatus/{id}")
    suspend fun deleteTechStatus(@Path("id")id:Int):Response<ResponseBody>
    //---------------------------->ManagemenUser
    //---------------------------->GetAllUser
    @GET("managementuser/getemployees")
    suspend fun getEmployees():Response<List<EmployeeData>>
    @GET("managementuser/getadmins")
    suspend fun getAdmins():Response<List<AdminData>>
    @GET("managementuser/gettechnicians")
    suspend fun getTechnicians():Response<List<TechnicianData>>
    @GET("managementuser/getchiefs")
    suspend fun getChiefs():Response<List<ChiefData>>
    //---------------------------->AddUser
    @POST("managementuser/addadmin")
    suspend fun addAdmin(@Body adminData: UserModel):Response<AdminData>
    @POST("managementuser/addtechnician")
    suspend fun addTechnician(@Body technicianData: UserModel):Response<TechnicianData>
    @POST("managementuser/addemployee")
    suspend fun addEmployee(@Body employeeData: UserModel):Response<EmployeeData>
    @POST("managementuser/addchief")
    suspend fun addChief(@Body chiefData: UserModel):Response<ChiefData>
    //---------------------------->EditUser
    @PUT("managementuser/updateadmin/{id}")
    suspend fun editAdmin(@Path("id") admin_id:Int,@Body adminData: UserModel):Response<AdminData>
    @PUT("managementuser/updateemployee/{id}")
    suspend fun editEmployee(@Path("id") emp_id:Int,@Body employeeData: UserModel):Response<EmployeeData>
    @PUT("managementuser/updatetechnician/{id}")
    suspend fun editTechnician(@Path("id") tech_id:Int,@Body technicianData: TechnicianBody):Response<TechnicianData>
    @PUT("managementuser/updatechief/{id}")
    suspend fun editChief(@Path("id") chief_id:Int,@Body chiefData: UserModel):Response<ChiefData>
    //----------------------------------> Get a specific user by ID
    @GET("managementuser/getadmin/{id}")
    suspend fun getAdminById(@Path("id") admin_id: Int): Response<AdminData>
    @GET("managementuser/getemployee/{id}")
    suspend fun getEmployeeById(@Path("id") emp_id: Int): Response<EmployeeData>
    @GET("managementuser/gettechnician/{id}")
    suspend fun getTechnicianById(@Path("id") eq_id: Int): Response<TechnicianData>
    @GET("managementuser/getchief/{id}")
    suspend fun getChiefById(@Path("id") eq_id: Int): Response<EmployeeData>
    //----------------------------------> Delete User

    @DELETE("managementuser/deleteadmin/{id}")
    suspend fun deleteAdminById(@Path("id") admin_id: Int): Response<messageBody>
    @DELETE("managementuser/deleteemployee/{id}")
    suspend fun deleteEmployeeById(@Path("id") emp_id: Int): Response<messageBody>
    @DELETE("managementuser/deletetechnician/{id}")
    suspend fun deleteTechnicianById(@Path("id") eq_id: Int): Response<messageBody>
    @DELETE("managementuser/deletechief/{id}")
    suspend fun deleteChiefById(@Path("id") eq_id: Int): Response<messageBody>
    //---------------------------------->authen api
    @POST("auth/login")
    suspend fun userLogin(@Body userRequest: UserRequest): Response<UserResponse>
    @GET("auth/logout")
    suspend fun userLogout():Response<logoutResponse>
    @POST("action/repair")
    suspend fun sendRequestForRepair(@Body sendRequest:sendRequest): Response<RequestResponse>
    //-------------------------->Display Api
    @GET("display/notification/{role}/{id}")
    suspend fun getNotification(@Path("role") role: String,@Path("id") id:Int):Response<notificationListResponse>
    @GET("display/request-list/{role}/{id}")
    suspend fun getRequestList(@Path("role") role : String,@Path("id") id:Int):Response<RequestListResponse>
    @GET("display/request/{id}")
    suspend fun getRequestDetail(@Path("id") id : Int):Response<detailRepairData>
    @GET("display/backlog-request-list")
    suspend fun getBackLogRequest():Response<BacklogResponse>
    @GET("display/techReceive/{id}")
    suspend fun getTechBacklogCount(@Path("id") id : Int):Response<TechBacklogCount>
    //-------------------------->Upload Image
    @Multipart
    @POST("images/upload")
    suspend fun uploadImage(@Part body: MultipartBody.Part): Response<UploadResponse>
    //-------------------------->Get Image
    @GET("images/{imageName}")
    suspend fun getImage(@Path("imageName") imageName: String): ResponseBody
    //-------------------------->Assign Work
    @POST("action/assign")
    suspend fun assignWork(@Body AssignWorkBody: AssignWorkBody):Response<messageBody>
    //-------------------------->Add DetailReceive
    @POST("action/addDetail")
    suspend fun addDetail(@Body addDetailRequest: addDetailRequest):Response<messageBody>
    @PUT("action/updateDetail/{id}")
    suspend fun updateDetail(@Path("id") id :Int,@Body updateDetailRequest: updateDetailRequest):Response<messageBody>

    //-------------------------->Check Connection
    @GET("/check_connection")
    suspend fun checkConnection(): ApiResponse

    //-------------------------->Report
    @GET("/report/dashboard-data")
    suspend fun getDashboardData():Response<DashboardModel>
}


data class ApiResponse(val success: Boolean)

data class messageBody(
    val message:String?
)

data class ErrorBody(
    val error:String?
)