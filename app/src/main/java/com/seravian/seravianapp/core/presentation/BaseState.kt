package com.seravian.seravianapp.core.presentation

data class BaseState(
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)