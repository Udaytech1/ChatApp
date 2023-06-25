package com.chatapp.compose.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

private const val TAG = "ChatApplication"

@HiltAndroidApp
class ChatApplication : Application() {
    companion object {
        var secondPerson = ""
    }
}