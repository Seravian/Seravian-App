package com.seravian.auth.data.repository

import com.seravian.auth.AuthState
import com.seravian.auth.util.ValidationError
import com.seravian.domain.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthStateRepository {
    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    fun updateEmail(email: String) {
        _authState.update {
            it.copy(
                email = email
            )
        }
    }

    fun updateEmailValidity(emailValidity: Result<Unit, ValidationError>) {
        _authState.update {
            it.copy(
                emailValidity = emailValidity
            )
        }
    }
}