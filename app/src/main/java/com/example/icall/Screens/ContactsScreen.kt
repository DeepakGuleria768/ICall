package com.example.icall.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.icall.R
import com.example.icall.ui.theme.homeScreenBackGroundColor
import com.example.icall.ui.theme.onlineStatusColor

@Composable
fun ContactList(navController: NavController) {

    val userContactDummyData = getContactList()
    // for know i am using the dummy static data

        LazyColumn(
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
            modifier = Modifier.background(color = Color.White).padding(top = 50.dp)
        ) {
            items(userContactDummyData) { data ->
                ContactListComponent(
                    name = data.name,
                    status = data.status,
                )
            }

    }

}

@Composable
fun ContactListComponent(name: String, status: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 18.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = homeScreenBackGroundColor),
            elevation = CardDefaults.elevatedCardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Text(text = name, fontSize = 18.sp, color = Color.Black)
                }
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.call),
                        contentDescription = "Call",
                        tint = if (status) onlineStatusColor else Color.Black
                    )
                }
            }
        }
    }
}

// Dummy Static Data
data class contactList(
    val id: String,
    val name: String,
    val status: Boolean
)

fun getContactList(): List<contactList> {
    return listOf(
        contactList("1", "Deepak Guleria", true),
        contactList("1", "Pratigya", false),
        contactList("1", "Rahul", true),
        contactList("1", "Daman", true),
        contactList("1", "Ankush", true),
        contactList("1", "Abhimanue", false),
        contactList("1", "ekta", false),
        contactList("1", "Doremon", false),
        contactList("1", "Atul", false),
        contactList("1", "Harish", true),
        contactList("1", "Shubash", false),
        contactList("1", "ghai", true),
        contactList("1", "Naman", true),
        contactList("1", "Pankaj", false),
        contactList("1", "Avni", false),
        contactList("1", "Divyam", false),
        contactList("1", "Darash", true),
        contactList("1", "Ankita", true),
        contactList("1", "Aditya", true),
        contactList("1", "Akash", false)
    )
}

