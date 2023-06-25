package com.chatapp.compose.data.repogistory

import android.util.Log
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import com.chatapp.compose.data.model.MessageData
import com.chatapp.compose.utils.FirebaseConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

private const val TAG = "ChatRepogistory"

class ChatRepogistory @Inject constructor(private val database: DatabaseReference) {
    val chatMessageList: MutableStateFlow<List<MessageData>> = MutableStateFlow(emptyList())
    fun getAllChat(mobile: String, receiver: String) {
        database.child(FirebaseConstants.Chats)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange: ${snapshot.value.toString()}")
                    if (snapshot.value != null) {
                        val list = ArrayList<MessageData>()

                        if (snapshot.hasChild("$receiver-$mobile")) {
                            val dataSnapshot = snapshot.child("$receiver-$mobile").children
                            for (data in dataSnapshot) {
                                list.add(
                                    data.getValue(MessageData::class.java)!!
                                )
                            }
                        } else if (snapshot.hasChild("$mobile-$receiver")) {
                            val dataSnapshot = snapshot.child("$mobile-$receiver").children
                            for (data in dataSnapshot) {
                                list.add(
                                    data.getValue(MessageData::class.java)!!
                                )
                            }
                        }

                        chatMessageList.value = list
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun sendMessage(mobile: String, receiver: String, messageData: MessageData) {
        val list = ArrayList<MessageData>()
        list.addAll(chatMessageList.value)
        list.add(messageData)
        database.child(FirebaseConstants.Chats)
            .addListenerForSingleValueEvent(object : OnValueChangeListener,
                ValueEventListener {
                override fun onValueChange(p0: NumberPicker?, p1: Int, p2: Int) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        if (snapshot.hasChild("$mobile-$receiver")) {
                            database.child(FirebaseConstants.Chats).child("$mobile-$receiver")
                                .setValue(list)
                        } else if (snapshot.hasChild("$receiver-$mobile")) {
                            database.child(FirebaseConstants.Chats).child("$receiver-$mobile")
                                .setValue(list)
                        } else {
                            database.child(FirebaseConstants.Chats).child("$mobile-$receiver")
                                .setValue(list)
                        }
                    } else {
                        database.child(FirebaseConstants.Chats).child("$mobile-$receiver")
                            .setValue(list)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}