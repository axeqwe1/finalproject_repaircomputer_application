package com.example.repaircomputerapplication_finalproject.component

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.repaircomputerapplication_finalproject.data.NavigateListForBtn
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


@Composable
fun MenuScreen(navController: NavController){
    //Put UI Here
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            androidx.compose.material3.Text(
                modifier = Modifier.padding(14.dp),
                text = "LastestWork",
                fontWeight = FontWeight.Bold
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray), contentAlignment = Alignment.Center){
                Lastest_List()
        }
        Box(modifier = Modifier.padding(24.dp))
        {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    androidx.compose.material3.Text(
                        modifier = Modifier.padding(0.dp),
                        text = "เมนูลัด",
                        fontWeight = FontWeight.Bold
                    )
                }
                Menu_List(navController)
            }
        }
    }
}

@Composable
fun Lastest_List(){
    LazyRow{
        val btnName = arrayOf("แจ้งซ่อม","รายงาน")
        items(btnName.size){ index ->
            OutlinedCard(modifier = Modifier
                .padding(18.dp)
                .clip(RoundedCornerShape(25))
                .clickable {}){
                Box(modifier = Modifier
                    .size(width = 175.dp, height = 175.dp)
                    .background(Color.Cyan)
                    .padding(24.dp)
                )
                {
                    Text("test")
                }
            }
        }
    }
}

@Composable
fun Menu_List(navController: NavController){
    val context = LocalContext.current
    val role = remember { mutableStateOf("") }
    LaunchedEffect(key1 = true){
        val dataStore = context.dataStore
        role.value = dataStore.data.map {
            items -> items[stringPreferencesKey("role")] ?: ""
        }.first()
    }
    Log.d(TAG, "Menu_List: $role")
    val navigateList = NavigateListForBtn().btnNavigate(role.value)
    LazyHorizontalGrid(rows = GridCells.Fixed(1)){
        items(navigateList.size){ index ->
            Box(modifier = Modifier
                .padding(20.dp)
                .clip(RoundedCornerShape(15))
                .background(Color.Cyan)
                .size(width = 125.dp, height = 125.dp)
                .clickable {
                    navController.navigate(navigateList[index].Route)
                    {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    androidx.wear.compose.material.Icon(
                        imageVector = navigateList[index].Icon,
                        contentDescription = navigateList[index].Btn_Name,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(navigateList[index].Btn_Name)
                }
            }
        }
    }
}