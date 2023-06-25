package com.chatapp.compose.ui.splash_screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chatapp.compose.ApplicationRoutes
import com.chatapp.compose.R
import com.chatapp.compose.ui.theme.AppLightColor
import com.chatapp.compose.ui.theme.ChatAppTheme
import com.chatapp.compose.utils.DatastoreHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

private const val TAG = "SplashScreen"

@Composable
fun SplashScreen(navController: NavController? = null) {
    Log.d(TAG, "SplashScreen: ")
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppLightColor)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.ic_app_foreground),
                modifier = Modifier
                    .size(250.dp),
                contentDescription = "AppLogo",
            )
        }
        checkAndNavigate(navController)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun checkAndNavigate(navController: NavController?) {
    val datastoreHelper = DatastoreHelper(LocalContext.current)
    var isLogin = false
    LaunchedEffect(key1 = "navigate") {
        isLogin = datastoreHelper.isUserLoggedIn.first()
        delay(1000)
        if (isLogin) {
            navController?.navigate(ApplicationRoutes.HOME_SCREEN) {
                popUpTo(0)
            }
        } else {
            navController?.navigate(ApplicationRoutes.LOGIN) {
                popUpTo(0)
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SplashScreenPreview() {
    ChatAppTheme {
        SplashScreen()
    }
}