package com.example.icall.Screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.view.View // Make sure you have this import for clarity
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser


@Composable
fun CallingScreen(navController: NavController) {

    val context = LocalContext.current

    Column (modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){


        var target by remember { mutableStateOf("") }

        var showCallButton by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = target,
            onValueChange = {target  =  it},
            label = {Text("Enter the userName to Call")},
            trailingIcon = {
                if (target.isNotBlank()) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        modifier = Modifier.clickable {
                            // First, validate if the user exists in Firebase
                            searchUserInFirebaseDatabase(context, target) { exists ->
                                if (exists) {
                                    showCallButton = true
                                } else {
                                    showCallButton = false
                                    Toast.makeText(context, "User not found in Firebase", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }

        )
        Spacer(Modifier.height(40.dp))

        if(showCallButton){
            AndroidView(
                modifier = Modifier.size(50.dp),
                factory = { context:Context ->
                      ZegoSendCallInvitationButton(context).apply {
                             setIsVideoCall(false)
                             resourceID = "zego_uikit_call"
                             setInvitees(
                                 listOf(ZegoUIKitUser(target,target)
                                 )
                             )
                      }  as View
            })
        }
    }
}

//function to search user in the firebase
fun searchUserInFirebaseDatabase(context: Context, userName:String, callBack:(Boolean)->Unit){
    val dbref = FirebaseDatabase.getInstance().getReference("users").child(userName)
    dbref.addListenerForSingleValueEvent(object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            callBack(snapshot.exists())
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            callBack(false)
        }

    })
}








