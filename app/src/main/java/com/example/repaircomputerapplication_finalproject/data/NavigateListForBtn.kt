package com.example.repaircomputerapplication_finalproject.data

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController



data class NavigateListForBtn(
    val Btn_Name: String = "",
    val Icon: ImageVector = Icons.Filled.List,
    val Route: String = ""
) {
    fun btnNavigate(Role: String): List<NavigateListForBtn> {
        var listType: List<NavigateListForBtn> = listOf()
        Log.d(TAG, "btnNavigate: $Role")

        when (Role) {
            "Technician" -> {
                listType = listOf(
                    NavigateListForBtn(
                        Btn_Name = "แจ้งซ่อม",
                        Icon = Icons.Filled.Build, // ไอคอนรูปเครื่องมือสำหรับช่างซ่อม
                        Route = ScreenRoutes.FormRequestForRepair.route
                    )
                )
            }
            "Employee" -> {
                listType = listOf(
                    NavigateListForBtn(
                        Btn_Name = "แจ้งซ่อม",
                        Icon = Icons.Filled.Build, // ไอคอนแจ้งปัญหาสำหรับพนักงาน
                        Route = ScreenRoutes.FormRequestForRepair.route
                    )
                )
            }
            "Admin" -> {
                listType = listOf(
                    NavigateListForBtn(
                        Btn_Name = "จ่ายงานที่ค้างเกิน 3 วัน",
                        Icon = Icons.Filled.EventBusy, // ไอคอนงานที่ค้าง
                        Route = ScreenRoutes.AssignWork.route
                    ),
                    NavigateListForBtn(
                        Btn_Name = "จัดการข้อมูล",
                        Icon = Icons.Filled.Settings, // ไอคอนการจัดการระบบ
                        Route = ScreenRoutes.ManageMenuNav.route
                    )
                )
            }
            "Chief" -> {
                listType = listOf(
                    NavigateListForBtn(
                        Btn_Name = "รายงาน",
                        Icon = Icons.Filled.BarChart, // ไอคอนแสดงรายงาน
                        Route = ScreenRoutes.Dashboard.route
                    )
                )
            }
        }
        return listType
    }
}

