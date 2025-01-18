package com.seravian.auth.data.repository

import com.seravian.auth.AuthState
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult
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

    fun updateEmailValidity(emailValidity: ValidationResult<Unit, ValidationError>) {
        _authState.update {
            it.copy(
                emailValidity = emailValidity
            )
        }
    }
}