package com.example.icall.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.icall.R
import com.example.icall.ui.theme.homeScreenBackGroundColor
import com.example.icall.ui.theme.topAppBarColor
import com.example.icall.ui.theme.topAppBarColorlight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = {Text("ICall")}, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = topAppBarColor
            ))
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(15.dp))
            HiUser("Deepak Guleria")
            Spacer(Modifier.height(100.dp))
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Options(R.drawable.call, "New Call", onClick = {navController.navigate("ContactScreen")})
                Options(R.drawable.history, "Call History", onClick = {navController.navigate("CallHistoryScreen")})
                Options(R.drawable.recording, "Recordings", onClick = {navController.navigate("RecodingScreen")})

            }
        }
    }
}

@Composable
fun HiUser(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(text = "Hi, ", fontSize = 40.sp, color = topAppBarColor, fontWeight = FontWeight.W800)
        Text(text = name, fontSize = 25.sp,color =topAppBarColorlight)
    }
}

@Composable
fun Options(icon: Int, name: String,onClick:()->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{onClick()}
            .padding(start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        ElevatedCard(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = homeScreenBackGroundColor
        ), elevation = CardDefaults.elevatedCardElevation(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.padding(30.dp),
                    painter = painterResource(id = icon),
                    contentDescription = "Call",
                    tint = Color.Black
                )
                Text(text = name, modifier = Modifier.padding(30.dp), color = Color.Black)
            }
        }
    }
}

