package com.example.repaircomputerapplication_finalproject.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadSuccessScreen(onContinueClicked: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Color.Green,
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = "Load Successful!",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onContinueClicked,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    )
}