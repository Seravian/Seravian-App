package com.seravian.seravianapp.features.auth.presentation.login

import androidx.compose.runtime.mutableStateOf
import com.seravian.seravianapp.core.presentation.BaseViewModel


class LoginViewModel(): BaseViewModel() {
    val emailState = mutableStateOf("")
    val passwordState = mutableStateOf("")
    val emailErrorState = mutableStateOf("")
    val passwordErrorState = mutableStateOf("")
    val navigation = mutableStateOf<LoginNavigation>(LoginNavigation.Idle)

    fun validateFields(): Boolean {

        if (emailState.value.isEmpty() || emailState.value.isBlank()) {
            emailErrorState.value = "Please Enter your Name"
            return false
        } else
            emailErrorState.value = ""

        if (passwordState.value.length < 6) {
            passwordErrorState.value = "Password Can't be less than 6 digits or characters"
            return false
        } else
            passwordErrorState.value = ""
        return true
    }

    fun navigateToRegister() {
        navigation.value = LoginNavigation.Register
    }
}