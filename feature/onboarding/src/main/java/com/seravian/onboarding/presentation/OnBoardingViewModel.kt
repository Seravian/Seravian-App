package com.seravian.onboarding.presentation

import androidx.lifecycle.viewModelScope
import com.greenvenom.validation.ValidateInput
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult
import com.greenvenom.validation.domain.onSuccess
import com.seravian.data.errors.AuthError
import com.seravian.onboarding.domain.OnBoardingRepository
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.data.onError
import com.seravian.ui.presentation.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val onBoardingRepository: OnBoardingRepository
): BaseViewModel() {
    private val _onBoardingState = MutableStateFlow(OnBoardingState())
    val userDetailsState = _onBoardingState.asStateFlow()

    private val _userDetailsExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _onBoardingState.value = _onBoardingState.value.copy(
            uploadingDetailsResult = Result.Error(AuthError(exception.message.toString())),
        )
        hideLoading()
        _onBoardingState.value.uploadingDetailsResult?.onError { showErrorMessage(it.message) }
    }

    fun userDetailsAction(action: OnBoardingAction) {
        when (action) {
            is OnBoardingAction.UpdateAuthUserType -> {
                updateUserDetailsState(
                    userType = action.type,
                )
            }
            is OnBoardingAction.UpdateAuthUserGender -> {
                updateUserDetailsState(
                    gender = action.gender,
                )
            }
            is OnBoardingAction.ValidateFullName -> {
                validateFullName(action.fullName)
            }
            is OnBoardingAction.ValidatePhoneNumber -> {
                validatePhoneNumber(
                    phoneNumber = action.phoneNumber,
                    countryCode = action.countryCode
                )
            }
            is OnBoardingAction.UpdateOnBoardingData -> {
                updateUserDetailsState(
                    fullName = action.fullName,
                    birthDate = action.birthDate,
                )
            }
            is OnBoardingAction.NavigateForm -> {
                updateUserDetailsState(
                    currentStep = if (action.isForward) {
                        _onBoardingState.value.currentStep + 1
                    } else {
                        _onBoardingState.value.currentStep - 1
                    }
                )
            }
            is OnBoardingAction.UploadDetailsAuth -> {
                uploadUserDetails(userDetails = _onBoardingState.value.userDetails)
            }
            is OnBoardingAction.ResetState -> {
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
        _onBoardingState.update {
            it.copy(
                fullNameValidationResult = validationResult
            )
        }
    }

    private fun validatePhoneNumber(
        phoneNumber: String,
        countryCode: String
    ) {
        val validationResult = ValidateInput.validateMobileNumber(phoneNumber, countryCode)
        validationResult.onSuccess {
            updateUserDetailsState(
                phoneNumber = it
            )
        }

        _onBoardingState.update {
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
        _onBoardingState.update { currentState ->
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
        userDetails: OnBoardingDetails
    ) {
        showLoading()
        viewModelScope.launch(_userDetailsExceptionHandler) {
            onBoardingRepository.updateUserDetails(
                fullName = userDetails.fullName ?: "Unknown",
                userType = userDetails.userType ?: "Unknown",
                phoneNumber = userDetails.phoneNumber ?: "Unknown",
                birthDate = userDetails.birthDate ?: "Unknown",
                gender = userDetails.gender ?: "Unknown"
            )
        }.invokeOnCompletion {
            _onBoardingState.value = _onBoardingState.value.copy(
                uploadingDetailsResult = Result.Success(Unit)
            )
            hideLoading()
        }
    }

    private fun resetState() {
        _onBoardingState.value = OnBoardingState()
    }
}