package com.chatapp.compose.data.model

sealed class NetworkResponse<T>(val data: T? = null, val error: String? = null) {
    class IdleState<T> : NetworkResponse<T>()
    class Loading<T> : NetworkResponse<T>()
    class Error<T>(error: String?) : NetworkResponse<T>(error = error)
    class Success<T>(data: T?) : NetworkResponse<T>(data = data)
}