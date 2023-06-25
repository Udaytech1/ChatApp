package com.chatapp.compose.ui.chat_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatapp.compose.application.ChatApplication
import com.chatapp.compose.data.model.MessageData
import com.chatapp.compose.data.repogistory.ChatRepogistory
import com.chatapp.compose.utils.DatastoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val chatRepogistory: ChatRepogistory,
    val datastoreHelper: DatastoreHelper,
) : ViewModel() {
    fun sendMessage(messageData: MessageData) {
        chatRepogistory.sendMessage(userPhone, ChatApplication.secondPerson, messageData)
    }

    private var userPhone = ""

    init {
        viewModelScope.launch {
            userPhone = datastoreHelper.userPhone.first()
        }
        getAllChatMessage()
    }

    private fun getAllChatMessage() {
        chatRepogistory.getAllChat(userPhone, ChatApplication.secondPerson)
    }

    val allChatResponse: MutableStateFlow<List<MessageData>> get() = chatRepogistory.chatMessageList
}

