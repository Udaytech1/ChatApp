package com.chatapp.compose.utils.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.database.annotations.Nullable

private const val TAG = "MyService"

class AppTerminateService : Service() {

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind: ")
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d(TAG, "onTaskRemoved: ")
        this.stopSelf()
    }
}