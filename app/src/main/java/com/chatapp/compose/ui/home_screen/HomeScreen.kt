package com.chatapp.compose.ui.home_screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.chatapp.compose.ApplicationRoutes
import com.chatapp.compose.R
import com.chatapp.compose.data.firebase_connection.FirebaseHelper
import com.chatapp.compose.data.repogistory.HomeRepogistory
import com.chatapp.compose.ui.comman_widgets.DashboardAppBar
import com.chatapp.compose.ui.home_screen.call_history.CallHistoryScreen
import com.chatapp.compose.ui.home_screen.connection_list.ConnectionScreen
import com.chatapp.compose.ui.home_screen.status_screen.ChatStatusScreen
import com.chatapp.compose.ui.theme.ChatAppTheme
import com.chatapp.compose.ui.theme.normalTextStyle
import com.chatapp.compose.utils.ContactHelper
import com.chatapp.compose.utils.DatastoreHelper
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "HomeScreen"

@OptIn(ExperimentalPagerApi::class)
@ExperimentalMaterial3Api
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(navController: NavController? = null, homeViewModel: HomeViewModel) {
    val tabMenu = listOf("Chats", "Status", "Call History")
    var tabIndex by remember { mutableStateOf(0) }

    var contactHelper: ContactHelper? = null
    if (ActivityCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        contactHelper = ContactHelper(LocalContext.current)
        RequestPermissionForContact(homeViewModel, contactHelper)
    } else {
        RequestPermissionForContact(homeViewModel, contactHelper)
    }

    Scaffold(
        topBar = {
            DashboardAppBar(searchTextChanged = {
                homeViewModel.serachConnection(it)
            })
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
            ) {
                Column() {
                    TabRow(
                        selectedTabIndex = tabIndex,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        tabMenu.forEachIndexed { index, s ->
                            Tab(
                                selected = tabIndex == index,
                                onClick = { tabIndex = index },
                            ) {
                                Box(modifier = Modifier.padding(all = 16.dp)) {
                                    Text(
                                        text = s,
                                        style = normalTextStyle,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    Surface(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                        when (tabIndex) {
                            0 -> {
                                ConnectionScreen(navController, homeViewModel)
                            }

                            1 -> {
                                ChatStatusScreen(navController, homeViewModel)
                            }

                            2 -> {
                                CallHistoryScreen(navController, homeViewModel)
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController?.navigate(ApplicationRoutes.CHAT_SCREEN + "1234567890")
            }, containerColor = colorResource(id = R.color.app_extra_light_color)) {
                Icon(
                    painterResource(id = R.drawable.chat_icon),
                    "",
                    tint = colorResource(id = R.color.black)
                )
            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RequestPermissionForContact(homeViewModel: HomeViewModel, contactHelper: ContactHelper?) {
    val context = LocalContext.current
    val contactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (it) {
                CoroutineScope(Dispatchers.IO).launch {
                    contactHelper?.getAllContact(homeViewModel)
                }
            }
        })
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                android.Manifest.permission.READ_CONTACTS
            )
        ) {

        } else {
            SideEffect {
                contactLauncher.launch(Manifest.permission.READ_CONTACTS)
                contactLauncher.launch(Manifest.permission.READ_PHONE_STATE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    contactLauncher.launch(Manifest.permission.READ_PHONE_NUMBERS)
                }
                contactLauncher.launch(Manifest.permission.READ_SMS)
            }
        }
    } else {
        CoroutineScope(Dispatchers.IO).launch {
            contactHelper?.getAllContact(homeViewModel)
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun HomeScreenPreview() {
    ChatAppTheme {
        val homeViewModel = HomeViewModel(
            HomeRepogistory(
                FirebaseHelper.getDatabaseReference(
                    LocalContext.current
                )
            ), DatastoreHelper(LocalContext.current)
        )
        HomeScreen(null, homeViewModel)
    }
}