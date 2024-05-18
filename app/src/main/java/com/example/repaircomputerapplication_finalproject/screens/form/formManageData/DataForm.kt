package com.example.repaircomputerapplication_finalproject.screens.form.formManageData

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel.DataManageViewModel


@Composable
fun DataForm(DataType:String?,isEdit:Boolean?,DataID:String?,navController: NavController,viewModel: DataManageViewModel = viewModel())
{
    when(DataType){
        "Building" -> buildingForm(isEdit,DataID,viewModel)
        "Department" -> departmentForm(isEdit,DataID,viewModel)
        "Equipment" -> equipmentForm(isEdit,DataID,viewModel)
        "EquipmentType" -> equipmentTypeForm(isEdit,DataID,viewModel)
        "LevelOfDamage" -> levelOfDamageForm(isEdit,DataID,viewModel)
        else -> {
            Text(text = "Not have DataType")}
    }
}