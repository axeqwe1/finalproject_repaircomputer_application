package com.example.repaircomputerapplication_finalproject.screens.manageDatascreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text


@Composable
fun MenuManagement(){
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(360.dp)
                .clickable{

                },
        ) {
            Text(text = "จัดการข้อมูลผู้ใช้งาน")
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Box(
            modifier = Modifier
                .size(360.dp)
                .clickable{

                },
        ) {
            Text(text = "จัดการข้อมูลทั่วไป")
        }
    }
}