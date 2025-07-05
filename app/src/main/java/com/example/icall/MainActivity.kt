package com.example.icall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.icall.Screens.CallHistoryScreen
import com.example.icall.Screens.ContactList
import com.example.icall.Screens.HomeScreen
import com.example.icall.Screens.RecodingScreen
import com.example.icall.ui.theme.ICallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            ICallTheme {
               ICall()
            }
        }
    }
}

@Composable
fun ICall(){
    // create the navGraph
    val navController = rememberNavController()
    NavHost(startDestination = "HomeScreen", navController = navController){
          composable("HomeScreen"){
              HomeScreen(navController = navController)
          }
        composable("ContactScreen"){
            ContactList(navController = navController)
        }
        composable("CallHistoryScreen"){
            CallHistoryScreen()
        }
        composable("RecodingScreen"){
            RecodingScreen()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ICallTheme {
ICall()
    }
}