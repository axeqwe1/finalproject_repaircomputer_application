package com.example.repaircomputerapplication_finalproject.`api-service`

import com.example.repaircomputerapplication_finalproject.model.BuildingData
import com.example.repaircomputerapplication_finalproject.model.EmployeeData
import com.example.repaircomputerapplication_finalproject.model.EquipmentData
import com.example.repaircomputerapplication_finalproject.model.RequestForRepairData
import com.example.repaircomputerapplication_finalproject.model.RequestListResponse
import com.example.repaircomputerapplication_finalproject.model.RequestResponse

import com.example.repaircomputerapplication_finalproject.model.UserRequest
import com.example.repaircomputerapplication_finalproject.model.UserResponse
import com.example.repaircomputerapplication_finalproject.model.sendRequest
import com.example.repaircomputerapplication_finalproject.model.UploadResponse
import com.example.repaircomputerapplication_finalproject.model.logoutResponse
import com.example.repaircomputerapplication_finalproject.model.notificationData
import com.example.repaircomputerapplication_finalproject.model.notificationListResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
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

    //---------------------------->ManagemenUser
    @GET("managementuser/getemployees")
    suspend fun getEmployees():Response<List<EmployeeData>>
    // Get a specific equipment by ID
    @GET("managementdata/getemployee/{id}")
    suspend fun getEmployeeById(@Path("id") eq_id: Int): Response<EmployeeData>

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


    //-------------------------->Upload Api
    @Multipart
    @POST("images/upload")
    suspend fun uploadImage(@Part body: MultipartBody.Part): Response<UploadResponse>
}