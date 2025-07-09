package com.example.icall.Screens

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log // Import Log for better error reporting
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoTranslationText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.icall.ui.theme.homeScreenBackGroundColor
// Keep this import for starting single calls later
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
// !!! IMPORTANT: Add this import for the Call Invitation Service !!!
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService
// You also need this import for the config if you're using Call Invitations
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import androidx.core.content.edit
import com.google.firebase.database.FirebaseDatabase
import com.zegocloud.uikit.ZegoUIKit

@Composable
fun RegisterUser(navController: NavController) {
    var enterUserName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        WelcomeText()
        EnterUserName(state = enterUserName, onStateChange = { enterUserName = it })
        Spacer(Modifier.height(10.dp))
        if (enterUserName.isNotBlank()) {
            StartButton(navController, enterUserName)
        }
    }
}

@Composable
fun WelcomeText() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome To ICall", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(5.dp))
        Text("Let's Make A ICall", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EnterUserName(state: String, onStateChange: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        OutlinedTextField(
            value = state,
            onValueChange = { onStateChange(it) },
            label = { Text("Enter Username") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
fun StartButton(navController: NavController, username: String) {
    val context = LocalContext.current

    ElevatedButton(
        onClick = {
            // 🔥 Save the username first
            val prefs = context.getSharedPreferences("iCallPrefs", Context.MODE_PRIVATE)
            prefs.edit() { putString("username", username) }

            // handle the permission for SYSTEM_ALERT_WINDOW
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                )
                if (context is Activity) {
                    context.startActivity(intent)
                    Toast.makeText(context, "Please grant overlay permission", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(
                        context,
                        "Cannot request overlay permission from this context",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                initLogin(context, username)
                // The Toast about "Login Success" will now come from within initLogin after ZegoCloud service init
                navController.navigate("HomeScreen")
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = homeScreenBackGroundColor),
        shape = RoundedCornerShape(3.dp)
    ) {
        Text("Start", color = Color.Black)
    }
}

fun initLogin(context: Context, username: String) {
    val appID: Long = 1618519306
    val appSign = "205a49d7b259031ef6ce3ccaccd0330e90edade5f18e5fe9a91ef38f06d73f7b"

    val applicationContext = context.applicationContext

    if (applicationContext is Application) {

        val config = ZegoUIKitPrebuiltCallInvitationConfig()


        // --- NEW ZegoCloud Initialization and User Login Approach ---
        // Initialize the ZegoCloud Prebuilt Call Invitation Service.
        // This service typically handles the user's login state directly within its initialization.
        ZegoUIKitPrebuiltCallService.init(
            applicationContext, // Use the Application context
            appID,
            appSign,
            username, // userID
            username, // userName
            // You can pass a config for the Call Invitation Service if needed for specific features
            config

        )
        Toast.makeText(context, "ZegoCloud Service Initialized!", Toast.LENGTH_SHORT).show()


        // --- Firebase database saving code (remains unchanged) ---
        val database = FirebaseDatabase.getInstance() // getting database instance
        val userRef = database.getReference("users")

        userRef.child(username).setValue(true)
            .addOnCompleteListener {
                Toast.makeText(
                    context,
                    "User '$username' registered in Firebase.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Failed to register user in Firebase: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("Firebase", "Failed to register user: ${e.message}") // Log the error for debugging
            }

    } else {
        Toast.makeText(
            context,
            "Failed to get Application context for ZegoCloud initialization.",
            Toast.LENGTH_LONG
        ).show()
        Log.e("ZegoCloud", "Failed to get Application context for ZegoCloud initialization.")
    }
}