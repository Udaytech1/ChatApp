package com.chatapp.compose.data.repogistory

import android.util.Log
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import com.chatapp.compose.data.model.LoginResponse
import com.chatapp.compose.data.model.UserDataModel
import com.chatapp.compose.utils.FirebaseConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

private const val TAG = "LoginRepogistory"

class LoginRepogistory @Inject constructor(private val database: DatabaseReference) {
    val loginResponse: MutableStateFlow<LoginResponse> = MutableStateFlow(LoginResponse())
    fun login(mobile: String) {
        database.child(FirebaseConstants.UsersRoot).child(mobile)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange: ${snapshot.value}")
                    if (snapshot.value == null) {
                        val listConnection = ArrayList<String>()
                        listConnection.add("1234567890")
                        val userData = UserDataModel(
                            mobile,
                            System.currentTimeMillis(),
                            listConnection,
                            "Active"
                        )
                        database.child(FirebaseConstants.UsersRoot).child(mobile).setValue(userData)

                        database.child(FirebaseConstants.UsersRoot).child("1234567890")
                            .addListenerForSingleValueEvent(object : OnValueChangeListener,
                                ValueEventListener {
                                override fun onValueChange(p0: NumberPicker?, p1: Int, p2: Int) {

                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.value != null) {
                                        val data = snapshot.getValue(UserDataModel::class.java)
                                        val list = ArrayList<String>()
                                        val connectList = data?.connectUser ?: emptyList()
                                        list.addAll(connectList)
                                        if (!list.contains(mobile)) {
                                            list.add(mobile)
                                            val user = data?.copy(connectUser = list)
                                            database.child(FirebaseConstants.UsersRoot)
                                                .child("1234567890").setValue(user)
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })
                    } else {
                        loginResponse.value = LoginResponse(
                            true,
                            snapshot.getValue(UserDataModel::class.java)?.phone ?: ""
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }
}