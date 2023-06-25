package com.chatapp.compose.ui.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatapp.compose.data.model.NetworkResponse
import com.chatapp.compose.data.repogistory.HomeRepogistory
import com.chatapp.compose.utils.ContactData
import com.chatapp.compose.utils.DatastoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepogistory: HomeRepogistory,
    private val datastoreHelper: DatastoreHelper,
) : ViewModel() {
    private var mobile = ""

    init {
        viewModelScope.launch {
            mobile = datastoreHelper.userPhone.first()
        }
        getAllContact()
    }

    private fun getAllContact() {
        homeRepogistory.getConnectUser(mobile)
    }

    private var mutableStateContact: MutableStateFlow<List<ContactData>> = MutableStateFlow(
        emptyList()
    )
    val contactUserList: MutableStateFlow<NetworkResponse<List<ContactData>>> get() = homeRepogistory.connectedUserList
    suspend fun addContact(contactData: ContactData) {
        println("adding contact ======== ")
        homeRepogistory.connectUser(mobile, contactData.phoneNumber)
    }

    fun serachConnection(it: String) {
        if (it.isNullOrEmpty()) {
            getAllContact()
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                delay(200)
                val list = ArrayList<ContactData>()
                for (i in contactUserList?.value?.data ?: emptyList()) {
                    if (i.phoneNumber.contains(it) || i.name.contains(it)) {
                        list.add(i)
                    }
                }
                contactUserList.value = NetworkResponse.Success(list)
            }
        }
    }
}