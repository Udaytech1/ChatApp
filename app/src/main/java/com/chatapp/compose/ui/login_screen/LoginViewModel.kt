package com.chatapp.compose.ui.login_screen

import androidx.lifecycle.ViewModel
import com.chatapp.compose.data.model.LoginResponse
import com.chatapp.compose.data.repogistory.LoginRepogistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val loginRepogistory: LoginRepogistory) : ViewModel() {
    fun login(mobile: String) {
        loginRepogistory.login(mobile)
    }

    val loginResponse: MutableStateFlow<LoginResponse> get() = loginRepogistory.loginResponse
}