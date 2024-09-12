package com.example.videocallapp.home

import android.telecom.InCallService.VideoCall
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.videocallapp.MainActivity
import com.example.videocallapp.R
import com.example.videocallapp.appId
import com.example.videocallapp.appSign
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.zegocloud.uikit.ZegoUIKit
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current as MainActivity
    LaunchedEffect(key1 = Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            context.initZegoInviteServices(appId, appSign, it.email!!, it.email!!)
        }

    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.back5_50), contentDescription = null,
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
        )
        Row(modifier = Modifier.padding(8.dp)) {
            Image(painter = painterResource(id = R.drawable.zoom), contentDescription = null, modifier = Modifier.size(40.dp))
            Image(painter = painterResource(id = R.drawable.textlogo), contentDescription = null, modifier = Modifier.size(90.dp).offset(y = -27.dp))
        }
             Column (modifier = Modifier.padding(top = 120.dp),
                 horizontalAlignment = Alignment.CenterHorizontally){
                 Text(text = "User ID : ${FirebaseAuth.getInstance().currentUser?.email}",
                     fontSize = 20.sp
                 )

             }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val targetUserId = remember { mutableStateOf("") }


            OutlinedTextField(value = targetUserId.value, onValueChange = {
                targetUserId.value = it
            }, label = { Text(text = "Add user email") },
                modifier = Modifier.fillMaxWidth()
            )
              Spacer(modifier = Modifier.size(40.dp))
            Row(verticalAlignment = Alignment.Bottom,
                modifier = Modifier.offset(y = 140.dp)){
                CallButtoon(isVideoCall = false) { button ->
                    if (targetUserId.value.isNotEmpty()) button.setInvitees(
                        mutableListOf(
                            ZegoUIKitUser(
                                targetUserId.value, targetUserId.value
                            )
                        )
                    )
                }
                Spacer(modifier = Modifier.size(60.dp))
                
                CallButtoon(isVideoCall = true) { button ->
                    if (targetUserId.value.isNotEmpty()) button.setInvitees(
                        mutableListOf(
                            ZegoUIKitUser(
                                targetUserId.value, targetUserId.value
                            )
                        )
                    )
                }
            }
        }
    }
}
@Composable
fun CallButtoon(isVideoCall: Boolean,onClick:(ZegoSendCallInvitationButton) -> Unit){
    AndroidView(factory = { context ->
            val button = ZegoSendCallInvitationButton(context)
            button.setIsVideoCall(isVideoCall)
        button.resourceID = "zego_data"
        button
    }, modifier = Modifier.size(50.dp)){zegoCallButton->
        zegoCallButton.setOnClickListener{_ -> onClick(zegoCallButton)}


    }
}
@Composable
@Preview(showBackground = true)
fun HomeScreenPreview(){
    HomeScreen(rememberNavController())
}