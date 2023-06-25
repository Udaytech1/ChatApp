package com.chatapp.compose.data.repogistory

import android.util.Log
import com.chatapp.compose.utils.FirebaseConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

private const val TAG = "UserRepogistory"

class UserRepogistory @Inject constructor(val database: DatabaseReference) {
    fun updateUserStatus(mobile: String, status: String) {
        if (mobile.isNullOrEmpty()) {
            return
        }
        database.child(FirebaseConstants.UsersRoot).child(mobile)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "UserRepogistory onDataChange: ${snapshot.value}")
                    if (snapshot.value != null) {
                        snapshot.ref.child("status").setValue(status)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: ")

                }
            })

    }
}