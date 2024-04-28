package com.example.repaircomputerapplication_finalproject.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen (navController: NavController){
        Scaffold(
            topBar = { TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Yellow),
            title = {Text("Top App Bar")}
        )},
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Bottom app bar",
                    )
                }
            }
        )
        {
            innerPading ->
            Column(
                modifier = Modifier.padding(innerPading),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                //Put UI Here
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                    Box(modifier = Modifier.padding(42.dp)){
                        Lastest_List()
                    }
                }
            Box(modifier = Modifier
                .wrapContentSize()
            ){
                
            }
            Menu_List()
            }
        }
}

@Composable
fun MenuList(){
    LazyColumn {
        item {
            Text("First Items")
        }
        items(5) {
            index -> Text(text = "Item:$index")
        }
        item {
            Text("Another Item")
        }
    }
}

@Composable
fun Lastest_List(){
    val btnName = arrayOf("แจ้งซ่อม","รายงาน")
        LazyRow{
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
fun Menu_List(){
    LazyHorizontalGrid(rows = GridCells.Fixed(2)){
        items(8){
                index ->
            Box(modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(15))
                .wrapContentSize(Alignment.TopStart)
            )
            {
                Box(modifier = Modifier
                    .size(width = 120.dp, height = 120.dp)
                    .background(Color.Cyan)
                    .padding(16.dp)
                ){

                }
            }
        }
    }
//    LazyRow{
//        items(8){
//            index ->
//            Box(modifier = Modifier
//                .padding(16.dp)
//                .clip(RoundedCornerShape(15))
//                .wrapContentSize(Alignment.TopStart)
//            )
//            {
//                Box(modifier = Modifier
//                    .size(width = 120.dp, height = 120.dp)
//                    .background(Color.Cyan)
//                    .padding(16.dp)
//                ){
//
//                }
//            }
//        }
//    }
}