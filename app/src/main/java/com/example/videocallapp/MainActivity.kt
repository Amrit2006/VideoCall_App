package com.example.videocallapp

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.videocallapp.home.HomeScreen
import com.example.videocallapp.login.LoginScreen
import com.example.videocallapp.login.SignUpScreen
import com.example.videocallapp.ui.theme.VideoCallAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.permissionx.guolindev.PermissionX
import com.zego.ve.Log
import com.zegocloud.uikit.internal.ZegoUIKitLanguage
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.event.CallEndListener
import com.zegocloud.uikit.prebuilt.call.event.ErrorEventsListener
import com.zegocloud.uikit.prebuilt.call.event.SignalPluginConnectListener
import com.zegocloud.uikit.prebuilt.call.event.ZegoCallEndReason
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallInvitationData
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoTranslationText
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider
import dagger.hilt.android.AndroidEntryPoint
import im.zego.zim.enums.ZIMConnectionEvent
import im.zego.zim.enums.ZIMConnectionState
import org.json.JSONObject
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window,false)
      //  installSplashScreen()
        setContent {
            VideoCallAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation()
                    }
                }
            }
        }

        permissionHandling(this)
    }

    fun initZegoInviteServices(appId: Long, appSign: String, userId: String, userName: String) {
        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()
        callInvitationConfig.translationText = ZegoTranslationText(ZegoUIKitLanguage.ENGLISH)
        callInvitationConfig.provider =
            ZegoUIKitPrebuiltCallConfigProvider { invitationData: ZegoCallInvitationData ? ->
                ZegoUIKitPrebuiltCallInvitationConfig.generateDefaultConfig(
                    invitationData
                )
            }

        ZegoUIKitPrebuiltCallService.events.errorEventsListener =
            ErrorEventsListener { errorCode: Int, message: String ->
                Timber.d("onError() called with: errorCode = [$errorCode], message = [$message]")
            }
        ZegoUIKitPrebuiltCallService.events.invitationEvents.pluginConnectListener =
            SignalPluginConnectListener { state: ZIMConnectionState, event: ZIMConnectionEvent, extendedData: JSONObject ->
                Timber.d("onSignalPluginConnectionStateChanged() called with: state = [$state], event = [$event], extendedData = [$extendedData$]")
            }
        ZegoUIKitPrebuiltCallService.init(
            application, appId, appSign, userId, userName, callInvitationConfig
        )
        ZegoUIKitPrebuiltCallService.enableFCMPush()

        ZegoUIKitPrebuiltCallService.events.callEvents.callEndListener =
            CallEndListener { callEndReason: ZegoCallEndReason?, jsonObject: String? ->

                Log.d(
                    "CallEndListener",
                    "Call Ended with reason: $callEndReason and json: $jsonObject"
                )
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        ZegoUIKitPrebuiltCallService.unInit()
    }

    private fun permissionHandling(activityContext: FragmentActivity) {
        PermissionX.init(activityContext).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
            .onExplainRequestReason { scope, deniedList ->
                val message =
                    "We need your consent for the following permissions in order to use the offline call function properly"
                scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
            }.request { allGranted, grantedList, deniedList -> }
    }
}

    // Determine the start destination based on authentication state

    @Composable
    fun AppNavigation() {
        val startDestination =
            if (FirebaseAuth.getInstance().currentUser != null) "home" else "login"
        NavHostScreen(startDestination)
    }

    @Composable
    fun NavHostScreen(startDestination: String) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = startDestination) {
            composable("login") {
                LoginScreen(navController)
            }
            composable("signup") {
                SignUpScreen(navController)
            }
            composable("home") {
                HomeScreen(navController)
            }
        }
    }


