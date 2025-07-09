package com.example.icall.Screens

import android.content.Context
import android.system.Os.remove
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavController
import com.example.icall.R
import com.example.icall.ui.theme.homeScreenBackGroundColor
import com.example.icall.ui.theme.topAppBarColor
import com.example.icall.ui.theme.topAppBarColorlight
import com.google.firebase.database.FirebaseDatabase
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val userName = getLoggedInUser(context)
    var expanded by remember { mutableStateOf(false) } // ← menu state
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ICall") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topAppBarColor
                ),
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu Icon",
                            tint = Color.White
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = homeScreenBackGroundColor
                    ) {
                        DropdownMenuItem(
                            text = { Text("Log Out", color = Color.Black) },
                            onClick = {
                                expanded = false
                                logOutUser(context = context, navController = navController)
                            },
                            colors = MenuDefaults.itemColors(homeScreenBackGroundColor)

                        )
                    }
                }
            )
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
            HiUser(name = userName)
            Spacer(Modifier.height(100.dp))
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Options(
                    R.drawable.call,
                    "New Call",
                    onClick = { navController.navigate("ContactScreen") })
                Options(
                    R.drawable.history,
                    "Call History",
                    onClick = { navController.navigate("CallHistoryScreen") })
                Options(
                    R.drawable.recording,
                    "Recordings",
                    onClick = { navController.navigate("RecodingScreen") })

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
        Text(text = name, fontSize = 25.sp, color = topAppBarColorlight)
    }
}

@Composable
fun Options(icon: Int, name: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                containerColor = homeScreenBackGroundColor
            ), elevation = CardDefaults.elevatedCardElevation(8.dp)
        ) {
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

//
fun getLoggedInUser(context: Context): String {
    val prefs = context.getSharedPreferences("iCallPrefs", Context.MODE_PRIVATE)
    return prefs.getString("username", "") ?: ""

}


fun logOutUser(context: Context, navController: NavController) {
    // Clear the local data from the shared preferences
    val prefs = context.getSharedPreferences("iCallPrefs", Context.MODE_PRIVATE)
    prefs.edit { remove("username") }

    // 2. deinitialize zego cloud services
    ZegoUIKitPrebuiltCallService.unInit()
    Toast.makeText(context, "ZegoCloud service uninitialized.", Toast.LENGTH_SHORT).show()

    // 3. Update Firebase Realtime Database (Remove user from 'users' node)
    // You'd ideally retrieve the current user's ID before logging out
    // For now, let's assume you have access to the username that was logged in.
    // A more robust solution would be to pass the actual logged-in username here.

    val loggedUserName = prefs.getString("username", null)
    if (loggedUserName != null) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(loggedUserName)
        userRef.removeValue() // remove the user entry from the database
            .addOnCompleteListener {
                Toast.makeText(
                    context,
                    "User '$loggedUserName' removed from Firebase.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Failed to remove user from Firebase: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    // 4. Navigate Back to Login/Registration Screen
    // Use popUpTo to clear the back stack so the user can't go back to the home screen

    navController.navigate("RegisterUserScreen") {
        popUpTo("HomeScreen") { inclusive = true } // remove the home screen from the backstack
    }
    Toast.makeText(context, "Logged out successfully.", Toast.LENGTH_SHORT).show()
}
