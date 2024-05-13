package com.example.repaircomputerapplication_finalproject.data

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController



data class NavigateListForBtn
(
    val Btn_Name : String = "",
    val Icon : ImageVector = Icons.Filled.List,
    val Route : String = ""
){
    fun btnNavigate(Role:String) : List<NavigateListForBtn>{
        var listType:List<NavigateListForBtn> = listOf()
        Log.d(TAG, "btnNavigate: $Role")
        if(Role == "Technician"){
            listType = listOf(
                NavigateListForBtn(
                    Btn_Name = "Technician",
                    Icon = Icons.Filled.Warning,
                    Route = ScreenRoutes.FormRequestForRepair.route
                ),
                NavigateListForBtn(
                    Btn_Name = "รานงาน",
                    Icon = Icons.Filled.Share
                ),
            )
        }
        if(Role == "Employee"){
            listType = listOf(
                NavigateListForBtn(
                    Btn_Name = "Employee",
                    Icon = Icons.Filled.Warning,
                    Route = ScreenRoutes.FormRequestForRepair.route
                ),
                NavigateListForBtn(
                    Btn_Name = "รานงาน",
                    Icon = Icons.Filled.Share
                ),
            )
        }
        if(Role == "Admin"){
            listType = listOf(
                NavigateListForBtn(
                    Btn_Name = "Admin",
                    Icon = Icons.Filled.Warning,
                    Route = ScreenRoutes.FormRequestForRepair.route
                ),
                NavigateListForBtn(
                    Btn_Name = "รานงาน",
                    Icon = Icons.Filled.Share
                ),
            )
        }
        if(Role == "Chief"){
            listType = listOf(
                NavigateListForBtn(
                    Btn_Name = "Chief",
                    Icon = Icons.Filled.Warning,
                    Route = ScreenRoutes.FormRequestForRepair.route
                ),
                NavigateListForBtn(
                    Btn_Name = "รานงาน",
                    Icon = Icons.Filled.Share
                ),
            )
        }

        return listType

    }
}
