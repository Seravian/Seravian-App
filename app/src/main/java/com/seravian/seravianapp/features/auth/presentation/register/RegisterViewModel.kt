package com.example.chatapp.screens.registers

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.chatapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(

) : BaseViewModel() {
    val emailState = mutableStateOf("")
    val passwordState = mutableStateOf("")
    val emailErrorState = mutableStateOf("")
    val passwordErrorState = mutableStateOf("")
    val fullNameState = mutableStateOf("")
    val fullNameErrorState = mutableStateOf("")
    val navigation = mutableStateOf<RegisterNavigation>(RegisterNavigation.Idle)

    fun validateFields(): Boolean {
        if (fullNameState.value.isEmpty() || fullNameState.value.isBlank()) {
            fullNameErrorState.value = "Please Enter Your Name"
            return false
        } else
            fullNameErrorState.value = ""

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


    fun navigateUp(){
        navigation.value = RegisterNavigation.NavigateUp
    }
}