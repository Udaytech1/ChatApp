package com.chatapp.compose

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.chatapp.compose.data.repogistory.UserRepogistory
import com.chatapp.compose.ui.theme.ChatAppTheme
import com.chatapp.compose.utils.DatastoreHelper
import com.chatapp.compose.utils.UserStatus
import com.chatapp.compose.utils.services.AppTerminateService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "MainActiivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var datastoreHelper: DatastoreHelper
    @Inject
    lateinit var userRepogistory: UserRepogistory

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                /* start service for listen event of termination */
                val intent = Intent(this, AppTerminateService::class.java)
                startService(intent)

                /* update user status */
                updateUserStatus(UserStatus.Active)
                // A surface container using the 'background' color from the theme
                AppNavGraph()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        /* update user status */
        updateUserStatus(UserStatus.Offline)
        Log.d(TAG, "--onDestroy: ")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d(TAG, "onBackPressed: ")
        updateUserStatus(UserStatus.Offline)
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    private fun updateUserStatus(status: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val phone = datastoreHelper.userPhone.first()
            userRepogistory.updateUserStatus(phone, status)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AppNavPreview() {
    ChatAppTheme {
        AppNavGraph()
    }
}