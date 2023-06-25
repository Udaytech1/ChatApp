package com.chatapp.compose.data.firebase_connection

import android.content.Context
import com.chatapp.compose.utils.FirebaseConstants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseHelper {
    private fun getFirebaseInstant(context: Context): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
    fun getDatabaseReference(context: Context): DatabaseReference {
        return getFirebaseInstant(context).getReference(FirebaseConstants.DatabaseName)
    }
}