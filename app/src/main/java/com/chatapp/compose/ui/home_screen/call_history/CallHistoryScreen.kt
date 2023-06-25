package com.chatapp.compose.ui.home_screen.call_history

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chatapp.compose.ApplicationRoutes
import com.chatapp.compose.R
import com.chatapp.compose.data.firebase_connection.FirebaseHelper
import com.chatapp.compose.data.model.NetworkResponse
import com.chatapp.compose.data.repogistory.HomeRepogistory
import com.chatapp.compose.ui.comman_widgets.CommonLoader
import com.chatapp.compose.ui.home_screen.HomeViewModel
import com.chatapp.compose.ui.theme.AppLightColor
import com.chatapp.compose.ui.theme.ChatAppTheme
import com.chatapp.compose.ui.theme.normalTextStyle
import com.chatapp.compose.utils.ContactData
import com.chatapp.compose.utils.DatastoreHelper

private val TAG = "ConnectionScreen"

@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
fun CallHistoryScreen(navController: NavController? = null, homeViewModel: HomeViewModel) {
    val userListResponse = homeViewModel.contactUserList.collectAsState()
    Surface(
        modifier = Modifier
            .background(color = AppLightColor)
    ) {
        when (userListResponse.value) {
            is NetworkResponse.Success -> {
                CallHistoryList(
                    userListResponse.value.data ?: emptyList(),
                    onUserClicked = { contactData ->
                        if (!contactData.phoneNumber.isEmpty())
                            navController?.navigate(ApplicationRoutes.CHAT_SCREEN + contactData.phoneNumber)
                    })
            }

            is NetworkResponse.Loading -> {
                CommonLoader(Modifier.fillMaxSize())
            }

            else -> {

            }
        }
    }

}

@Composable
private fun CallHistoryList(userList: List<ContactData>, onUserClicked: (ContactData) -> Unit) {
    LazyColumn {
        items(userList.size, key = { index ->
            userList.get(index).phoneNumber
        }) { index ->
            val user = userList[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 7.dp, top = 7.dp)
                    .clickable {
                        onUserClicked(user)
                    },
                shape = CircleShape,
                colors = CardDefaults.cardColors(colorResource(id = R.color.app_extra_light_color))
            ) {
                Box(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxHeight()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_video),
                            contentDescription = "",
                            modifier = Modifier
                                .size(35.dp),
                            contentScale = ContentScale.Crop

                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = user.phoneNumber,
                            color = Color.Black,
                            style = normalTextStyle,
                        )
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = "03:00 AM",
                                color = Color.Black,
                                style = normalTextStyle,
                            )
                        }
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectionScreenPreview() {
    ChatAppTheme {
        CallHistoryScreen(
            null,
            HomeViewModel(
                HomeRepogistory(
                    FirebaseHelper.getDatabaseReference(
                        LocalContext.current
                    )
                ), DatastoreHelper(LocalContext.current)
            )
        )
    }
}