package com.greenvenom.auth.data.repository

import com.greenvenom.auth.presentation.EmailState
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EmailStateRepository {
    private val _emailState = MutableStateFlow(EmailState())
    val emailState = _emailState.asStateFlow()

    fun updateEmail(email: String) {
        _emailState.update {
            it.copy(
                email = email
            )
        }
    }

    fun updateEmailValidity(emailValidity: ValidationResult<Unit, ValidationError>) {
        _emailState.update {
            it.copy(
                emailValidity = emailValidity
            )
        }
    }

    fun resetEmailState() {
        _emailState.update { EmailState() }
    }
}