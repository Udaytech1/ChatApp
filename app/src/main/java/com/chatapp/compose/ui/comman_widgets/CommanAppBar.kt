package com.chatapp.compose.ui.comman_widgets

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chatapp.compose.R
import com.chatapp.compose.ui.theme.WhiteColor
import com.chatapp.compose.ui.theme.normalTextStyle
import com.chatapp.compose.ui.theme.titleLarge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommanAppBar(title: String, onStartIconClicked: () -> Unit) {
    TopAppBar(title = {
        Text(text = title, color = WhiteColor, style = titleLarge)
    }, navigationIcon = {
        IconButton(onClick = { onStartIconClicked() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "", tint = WhiteColor)
        }
    }, colors = TopAppBarDefaults.largeTopAppBarColors(colorResource(id = R.color.app_color)))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardAppBar(searchTextChanged: (String) -> Unit) {
    val searchBoxVisible = remember {
        mutableStateOf(false)
    }
    val searchText = remember {
        mutableStateOf("")
    }
    if (searchBoxVisible.value) {
        OutlinedTextField(
            value = searchText.value,
            onValueChange = {
                searchText.value = it
                searchTextChanged(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.app_color)),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    searchText.value = ""
                    searchBoxVisible.value = false
                    searchTextChanged("")
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = WhiteColor
                    )
                }
            },
            placeholder = {
                Text(
                    text = "Search contact..",
                    color = WhiteColor,
                    style = normalTextStyle,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onSearch = KeyboardActions.Default.onSearch),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
        )
    } else {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Personal chat", color = WhiteColor, style = titleLarge)
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                        IconButton(onClick = {
                            searchBoxVisible.value = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "",
                                tint = WhiteColor
                            )
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(colorResource(id = R.color.app_color)),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAppBar(
    mobileNumber: String,
    callClicked: () -> Unit,
    backButtonClicked: () -> Unit,
    videoCall: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = mobileNumber, color = WhiteColor, style = titleLarge)
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                    Row() {
                        IconButton(onClick = {
                            videoCall()
                        }) {
                            Icon(
                                painterResource(id = R.drawable.ic_video),
                                contentDescription = "video call",
                                tint = WhiteColor
                            )
                        }
                        IconButton(onClick = {
                            callClicked()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "",
                                tint = WhiteColor
                            )
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(colorResource(id = R.color.app_color)),
        navigationIcon = {
            IconButton(onClick = { backButtonClicked() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = WhiteColor
                )
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun CommanAppBarPreview() {
    CommanAppBar(title = "Login") {

    }
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun DashboardAppBarPreview() {
    DashboardAppBar(searchTextChanged = {

    })
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun DashboardChatPreview() {
    ChatAppBar("45698456456", {}, {}, {})
}