package com.seravian.auth.presentation

import androidx.lifecycle.viewModelScope
import com.seravian.auth.domain.repository.AccountRepository
import com.seravian.ui.presentation.BaseViewModel
import kotlinx.coroutines.launch

class AccountViewModel(
    private val accountRepository: AccountRepository
): BaseViewModel() {
    fun accountAction(action: AccountAction) {
        when(action) {
            is AccountAction.Logout -> logoutUser()
        }
    }

    private fun logoutUser() {
        viewModelScope.launch {
            accountRepository.logoutUser()
        }
    }
}