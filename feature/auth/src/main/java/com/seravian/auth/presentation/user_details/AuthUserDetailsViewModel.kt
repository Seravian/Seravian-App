package com.seravian.auth.presentation.user_details

import androidx.lifecycle.viewModelScope
import com.seravian.auth.data.AuthError
import com.seravian.auth.data.AuthUserDetails
import com.seravian.auth.domain.repository.UserDetailsRepository
import com.seravian.domain.network.Result
import com.seravian.domain.network.onError
import com.seravian.ui.presentation.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthUserDetailsViewModel(
    private val userDetailsRepository: UserDetailsRepository
): BaseViewModel() {
    private val _authUserDetailsState = MutableStateFlow(AuthUserDetailsState())
    val userDetailsState = _authUserDetailsState.asStateFlow()

    private val _userDetailsExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _authUserDetailsState.value = _authUserDetailsState.value.copy(
            uploadingDetailsResult = Result.Error(AuthError(exception.message.toString())),
        )
        hideLoading()
        _authUserDetailsState.value.uploadingDetailsResult?.onError { showErrorMessage(it.message) }
    }

    fun userDetailsAction(action: AuthUserDetailsAction) {
        when (action) {
            is AuthUserDetailsAction.UpdateAuthUserType -> {
                updateUserDetailsState(
                    userType = action.userType,
                )
            }
            is AuthUserDetailsAction.UpdateAuthUserGender -> {
                updateUserDetailsState(
                    gender = action.userGender,
                )
            }
            is AuthUserDetailsAction.UpdateAuthUserDetails -> {
                updateUserDetailsState(
                    fullName = action.userFullName,
                    phoneNumber = action.userPhoneNumber,
                    birthDate = action.userBirthDate,
                )
            }
            is AuthUserDetailsAction.NavigateForm -> {
                updateUserDetailsState(
                    currentStep = if (action.isForward) {
                        _authUserDetailsState.value.currentStep + 1
                    } else {
                        _authUserDetailsState.value.currentStep - 1
                    }
                )
            }
            is AuthUserDetailsAction.UploadDetailsAuth -> {
                uploadUserDetails(userDetails = _authUserDetailsState.value.userDetails)
            }
            is AuthUserDetailsAction.ResetState -> {
                resetState()
            }
        }
    }

    private fun updateUserDetailsState(
        currentStep: Int? = null,
        fullName: String? = null,
        userType: String? = null,
        phoneNumber: String? = null,
        birthDate: String? = null,
        gender: String? = null,
    ) {
        _authUserDetailsState.update { currentState ->
            currentState.copy(
                currentStep = currentStep ?: currentState.currentStep,
                userDetails = currentState.userDetails.copy(
                    fullName = fullName ?: currentState.userDetails.fullName,
                    userType = userType ?: currentState.userDetails.userType,
                    phoneNumber = phoneNumber ?: currentState.userDetails.phoneNumber,
                    birthDate = birthDate ?: currentState.userDetails.birthDate,
                    gender = gender ?: currentState.userDetails.gender
                )
            ).let { updatedState ->
                updatedState.copy(
                    isDetailsFull = updatedState.userDetails.fullName != null &&
                            updatedState.userDetails.userType != null &&
                            updatedState.userDetails.phoneNumber != null &&
                            updatedState.userDetails.birthDate != null &&
                            updatedState.userDetails.gender != null
                )
            }
        }
    }

    private fun uploadUserDetails(
        userDetails: AuthUserDetails
    ) {
        showLoading()
        viewModelScope.launch(_userDetailsExceptionHandler) {
            userDetailsRepository.updateUserDetails(
                fullName = userDetails.fullName ?: "Unknown",
                userType = userDetails.userType ?: "Unknown",
                phoneNumber = userDetails.phoneNumber ?: "Unknown",
                birthDate = userDetails.birthDate ?: "Unknown",
                gender = userDetails.gender ?: "Unknown"
            )
        }.invokeOnCompletion {
            _authUserDetailsState.value = _authUserDetailsState.value.copy(
                uploadingDetailsResult = Result.Success(Unit)
            )
            hideLoading()
        }
    }

    private fun resetState() {
        _authUserDetailsState.value = AuthUserDetailsState()
    }
}