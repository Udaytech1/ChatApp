package com.chatapp.compose.data.model

data class UserDataModel(
    val phone: String = "",
    val lastLoginTime: Long = 0L,
    val connectUser: List<String>? = null,
    val status: String = "",
)