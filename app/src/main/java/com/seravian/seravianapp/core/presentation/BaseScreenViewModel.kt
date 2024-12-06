package com.seravian.seravianapp.core.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseScreenViewModel: ViewModel() {
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    fun showLoading() {
        _loadingState.value = true
    }

    fun hideLoading() {
        _loadingState.value = false
    }

    fun showErrorMessage(errorMessage: String) {
        _errorMessage.value = errorMessage
    }

    fun hideErrorMessage() {
        _errorMessage.value = ""
    }
}