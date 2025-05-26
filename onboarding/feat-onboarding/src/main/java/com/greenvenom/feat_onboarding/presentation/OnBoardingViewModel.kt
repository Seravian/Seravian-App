package com.greenvenom.feat_onboarding.presentation

import androidx.lifecycle.viewModelScope
import com.greenvenom.core_onboarding.data.dto.request.OnBoardingRequest
import com.greenvenom.validation.ValidateInput
import com.greenvenom.validation.domain.onSuccess
import com.greenvenom.feat_onboarding.domain.repository.OnBoardingRepository
import com.greenvenom.core_ui.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val onBoardingRepository: OnBoardingRepository
): BaseViewModel() {
    private val _onBoardingState = MutableStateFlow(OnBoardingState())
    val userDetailsState = _onBoardingState.asStateFlow()

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
            is OnBoardingAction.ResetNetworkResult -> {
                resetNetworkResult()
            }
        }
    }

    private fun validateFullName(fullName: String) {
        val trimmedName = fullName.trim()
        val validationResult = ValidateInput.validateFullName(trimmedName)
        validationResult.onSuccess {
            updateUserDetailsState(
                fullName = trimmedName
            )
        }
        _onBoardingState.update {
            it.copy(
                fullNameValidationResult = validationResult
            )
        }
    }

    private fun updateUserDetailsState(
        currentStep: Int? = null,
        fullName: String? = null,
        userType: Int? = null,
        birthDate: String? = null,
        gender: Int? = null,
    ) {
        _onBoardingState.update { currentState ->
            currentState.copy(
                currentStep = currentStep ?: currentState.currentStep,
                userDetails = currentState.userDetails.copy(
                    fullName = fullName ?: currentState.userDetails.fullName,
                    role = userType ?: currentState.userDetails.role,
                    dateOfBirth = birthDate?.trim() ?: currentState.userDetails.dateOfBirth,
                    gender = gender ?: currentState.userDetails.gender
                )
            ).let { updatedState ->
                updatedState.copy(
                    isDataFull = updatedState.userDetails.fullName != null &&
                            updatedState.userDetails.role != null &&
                            updatedState.userDetails.dateOfBirth != null &&
                            updatedState.userDetails.gender != null
                )
            }
        }.let {
            if (this.userDetailsState.value.isDataFull) {
                uploadUserDetails(this.userDetailsState.value.userDetails)
            }
        }
    }

    private fun uploadUserDetails(
        userDetails: OnBoardingDetails
    ) {
        viewModelScope.launch {
            val result = onBoardingRepository.updateUserDetails(
                onBoardingRequest = OnBoardingRequest(
                    fullName = userDetails.fullName ?: "",
                    dateOfBirth = userDetails.dateOfBirth ?: "",
                    gender = userDetails.gender ?: -1,
                    role = userDetails.role ?: -1
                )
            )
            _onBoardingState.update {
                it.copy(
                    uploadingDetailsResult = result
                )
            }
        }
    }

    private fun resetState() {
        _onBoardingState.value = OnBoardingState()
    }

    private fun resetNetworkResult() {
        _onBoardingState.update {
            it.copy(
                uploadingDetailsResult = null
            )
        }
    }
}