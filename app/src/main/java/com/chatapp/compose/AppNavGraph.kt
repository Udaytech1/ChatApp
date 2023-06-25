package com.chatapp.compose

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chatapp.compose.application.ChatApplication
import com.chatapp.compose.ui.chat_screen.ChatScreen
import com.chatapp.compose.ui.chat_screen.ChatViewModel
import com.chatapp.compose.ui.home_screen.HomeScreen
import com.chatapp.compose.ui.home_screen.HomeViewModel
import com.chatapp.compose.ui.login_screen.LoginScreen
import com.chatapp.compose.ui.login_screen.LoginViewModel
import com.chatapp.compose.ui.splash_screen.SplashScreen
import com.chatapp.compose.utils.DatastoreHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "AppNavGraph"

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun AppNavGraph(navHostController: NavHostController = rememberNavController()) {
    val datastoreHelper = DatastoreHelper(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()
    var phone = ""
    coroutineScope.launch(Dispatchers.IO) {
        datastoreHelper.userPhone.collect {
            phone = it
        }
    }
    NavHost(navController = navHostController, startDestination = ApplicationRoutes.SPLASH) {
        composable(ApplicationRoutes.SPLASH) {
            SplashScreen(navHostController)
        }
        composable(ApplicationRoutes.LOGIN) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                navHostController, loginViewModel
            )
        }
        composable(ApplicationRoutes.HOME_SCREEN) {
            Log.d(TAG, "AppNavGraph: HOME_SCREEN $phone")
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(navHostController, homeViewModel)
        }
        composable(
            ApplicationRoutes.CHAT_SCREEN + "{secondPerson}", arguments = listOf(
                navArgument("secondPerson") {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            ChatApplication.secondPerson = backStackEntry.arguments?.getString("secondPerson") ?: ""
            val chatViewModel: ChatViewModel = hiltViewModel()
            ChatScreen(
                navHostController,
                chatViewModel,
                backStackEntry.arguments?.getString("secondPerson") ?: "",
                phone
            )
        }
    }

}
