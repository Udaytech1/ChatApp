package com.chatapp.compose.data.repogistory

import android.util.Log
import com.chatapp.compose.data.model.NetworkResponse
import com.chatapp.compose.data.model.UserDataModel
import com.chatapp.compose.utils.ContactData
import com.chatapp.compose.utils.FirebaseConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


private const val TAG = "HomeRepogistory"

class HomeRepogistory @Inject constructor(private val database: DatabaseReference) {
    val connectedUserList: MutableStateFlow<NetworkResponse<List<ContactData>>> =
        MutableStateFlow(NetworkResponse.IdleState())

    fun getConnectUser(mobile: String) {
        if (mobile.isEmpty()) {
            return
        }
        connectedUserList.value = NetworkResponse.Loading()
        database.child(FirebaseConstants.UsersRoot)/*.child(mobile)*/.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: getUser ${snapshot.value}")
                val list = ArrayList<ContactData>()
                for (i in snapshot.children) {
                    val userDataModel = i.getValue(UserDataModel::class.java)
                    if (mobile != userDataModel?.phone) {
                        list.add(
                            ContactData(
                                "-1",
                                /*Commons.getRandomString(Random.nextInt(8))?:*/
                                "Ram",
                                userDataModel?.phone ?: "",
                                null,
                                userDataModel?.status ?: ""
                            )
                        )
                    }

                }
                connectedUserList.value = NetworkResponse.Success(list)
            }

            override fun onCancelled(error: DatabaseError) {
                connectedUserList.value = NetworkResponse.Error(error.message)
            }
        })
    }

    suspend fun connectUser(mobile: String, secondPerson: String) {
        database.child(FirebaseConstants.UsersRoot).child(secondPerson)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange connectUser: $secondPerson ${snapshot.value}")
                    if (snapshot.value != null) {
                        database.child(FirebaseConstants.UsersRoot).child(mobile)
                            .addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    Log.d(TAG, "onDataChange data to be added: ${snapshot.value}")
                                    if (snapshot.value != null) {
                                        val data = snapshot.getValue(UserDataModel::class.java)
                                        val list = ArrayList<String>()
                                        val connectList = data?.connectUser ?: emptyList()
                                        list.addAll(connectList)
                                        if (!list.contains(mobile)) {
                                            list.add(mobile)
                                            val user = data?.copy(connectUser = list)
                                            database.child(FirebaseConstants.UsersRoot)
                                                .child(mobile).setValue(user)
                                        }
                                        Log.d(TAG, "onDataChange After added: ${snapshot.value}")
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.d(TAG, "onCancelled: Adding ${error.message}")
                                }
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}