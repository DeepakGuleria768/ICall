package com.example.icall

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.icall.Screens.CallHistoryScreen
import com.example.icall.Screens.CallingScreen
import com.example.icall.Screens.HomeScreen
import com.example.icall.Screens.RecodingScreen
import com.example.icall.Screens.RegisterUser
import com.example.icall.ui.theme.ICallTheme
import com.zegocloud.uikit.ZegoUIKit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            ICallTheme{
               ICall()
            }
        }
    }


}

@Composable
fun ICall(){

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("iCallPrefs", Context.MODE_PRIVATE)
    val savedUsername = prefs.getString("username", "")

    // create the navGraph
    val navController = rememberNavController()
    NavHost(startDestination = if(!savedUsername.isNullOrBlank()) "HomeScreen" else "RegisterUserScreen", navController = navController){
          composable("HomeScreen"){
              HomeScreen(navController = navController)
          }
        composable("ContactScreen"){
            CallingScreen(navController = navController)
        }
        composable("CallHistoryScreen"){
            CallHistoryScreen()
        }
        composable("RecodingScreen"){
            RecodingScreen()
        }
        composable("RegisterUserScreen"){
            RegisterUser(navController = navController)
        }

    }
}



