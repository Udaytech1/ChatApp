package com.chatapp.compose.ui.chat_screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chatapp.compose.R
import com.chatapp.compose.data.firebase_connection.FirebaseHelper
import com.chatapp.compose.data.model.MessageData
import com.chatapp.compose.data.repogistory.ChatRepogistory
import com.chatapp.compose.ui.comman_widgets.ChatAppBar
import com.chatapp.compose.ui.theme.ChatAppTheme
import com.chatapp.compose.ui.theme.chatTextStyle
import com.chatapp.compose.utils.DatastoreHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ChatScreen(
    navController: NavController? = null,
    chatViewModel: ChatViewModel,
    secondPerson: String,
    userPhone: String,
) {

    val inputMessageState = remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            ChatAppBar(
                mobileNumber = secondPerson,
                callClicked = { },
                backButtonClicked = { navController?.popBackStack() },
                videoCall = {
                }
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues)
            ) {
                ChatList(chatViewModel, userPhone)
                Box(
                    modifier = Modifier.wrapContentWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = inputMessageState.value, onValueChange = {
                                inputMessageState.value = it
                            },
                            placeholder = {
                                Text(text = stringResource(R.string.write_message))
                            },
                            shape = CircleShape,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .weight(1f),
                            maxLines = 3,
                            trailingIcon = {
                                IconButton(onClick = {
                                    if (inputMessageState.value.trim().isEmpty()) {
                                        return@IconButton
                                    }
                                    chatViewModel.sendMessage(
                                        MessageData(
                                            inputMessageState.value.trim(),
                                            userPhone,
                                            System.currentTimeMillis()
                                        )
                                    )
                                    inputMessageState.value = ""
                                }, modifier = Modifier.size(50.dp)) {
                                    Icon(
                                        Icons.Default.Send,
                                        contentDescription = "",
                                        modifier = Modifier.padding(end = 5.dp)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
private fun ChatList(chatViewModel: ChatViewModel, userPhone: String) {
    val list = chatViewModel.allChatResponse.collectAsState()
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Surface(modifier = Modifier.padding(bottom = 80.dp, top = 10.dp)) {
        LazyColumn(
            state = scrollState,
        ) {
            coroutineScope.launch {
                if (list.value.size > 0)
                    scrollState.animateScrollToItem(list.value.size)
            }

            items(list.value.size) { index: Int ->
                val message = list.value[index]
                if (message.sentBy != userPhone) {
                    Card(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(start = 16.dp, top = 8.dp, end = 60.dp, bottom = 8.dp),
                        shape = RoundedCornerShape(
                            topEnd = 10.dp,
                            topStart = 10.dp,
                            bottomStart = 10.dp,
                            bottomEnd = 20.dp
                        )
                    ) {
                        Text(
                            text = message.message,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 7.dp, bottom = 7.dp),
                            color = colorResource(id = R.color.primary_color),
                            style = chatTextStyle
                        )
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Card(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .padding(start = 60.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
                            shape = RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp,
                                bottomEnd = 10.dp,
                                bottomStart = 20.dp
                            )
                        ) {
                            Text(
                                text = message.message,
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 7.dp, bottom = 7.dp),
                                color = Color.Gray,
                                style = chatTextStyle
                            )
                        }
                    }
                }
            }
        }
        // observer when reached end of list
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ChatScreenPreview() {
    ChatAppTheme {
        ChatScreen(
            null, ChatViewModel(
                ChatRepogistory(
                    FirebaseHelper.getDatabaseReference(
                        LocalContext.current
                    )
                ), DatastoreHelper(LocalContext.current)
            ), "", ""
        )
    }
}