package com.example.chatapp.base

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {

    val loadingState = mutableStateOf(false)
    val dialogState = mutableStateOf("")

    fun showLoading(){
        loadingState.value = true
    }
    fun hideLoading(){
        loadingState.value = false
    }
    fun showErrorMessage(errorMessage: String){
        dialogState.value = errorMessage
    }
    fun hideErrorMessage(){
        dialogState.value = ""
    }
}