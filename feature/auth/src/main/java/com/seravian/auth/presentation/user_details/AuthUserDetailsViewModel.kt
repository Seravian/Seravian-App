package com.seravian.auth.presentation.user_details

import androidx.lifecycle.viewModelScope
import com.arpitkatiyarprojects.countrypicker.utils.CountryPickerUtils
import com.seravian.auth.data.AuthError
import com.seravian.auth.data.AuthUserDetails
import com.seravian.auth.domain.repository.UserDetailsRepository
import com.seravian.auth.util.ValidateInput
import com.seravian.auth.util.ValidationError
import com.seravian.domain.network.Result
import com.seravian.domain.network.onError
import com.seravian.domain.network.onSuccess
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
                    userType = action.type,
                )
            }
            is AuthUserDetailsAction.UpdateAuthUserGender -> {
                updateUserDetailsState(
                    gender = action.gender,
                )
            }
            is AuthUserDetailsAction.ValidateFullName -> {
                validateFullName(action.fullName)
            }
            is AuthUserDetailsAction.ValidatePhoneNumber -> {
                validatePhoneNumber(
                    phoneNumber = action.phoneNumber,
                    countryCode = action.countryCode
                )
            }
            is AuthUserDetailsAction.UpdateAuthUserDetails -> {
                updateUserDetailsState(
                    fullName = action.fullName,
                    birthDate = action.birthDate,
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

    private fun validateFullName(fullName: String) {
        val validationResult = ValidateInput.validateFullName(fullName)
        validationResult.onSuccess {
            updateUserDetailsState(
                fullName = fullName
            )
        }
        _authUserDetailsState.update {
            it.copy(
                fullNameValidationResult = validationResult
            )
        }
    }

    private fun validatePhoneNumber(
        phoneNumber: String,
        countryCode: String
    ) {
        val isValid = CountryPickerUtils.isMobileNumberValid(phoneNumber, countryCode)
        val validationResult =  if (isValid) {
            val formattedNumber = CountryPickerUtils.getFormattedMobileNumber(phoneNumber, countryCode)
            updateUserDetailsState(
                phoneNumber = formattedNumber
            )
            Result.Success(Unit)
        } else {
            Result.Error(ValidationError.INVALID_PHONE_NUMBER)
        }

        _authUserDetailsState.update {
            it.copy(
                mobileNumberValidationResult = validationResult
            )
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