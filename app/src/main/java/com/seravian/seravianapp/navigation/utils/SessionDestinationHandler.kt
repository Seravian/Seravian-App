package com.seravian.seravianapp.navigation.utils

import com.greenvenom.core_navigation.data.NavigationType.ClearBackStack
import com.greenvenom.core_navigation.data.repository.NavigationStateRepository
import com.greenvenom.core_network.data.SessionDestinations
import com.greenvenom.core_network.domain.repository.SessionRepository
import com.seravian.core_profile.domain.utils.Role
import com.seravian.feat_navigation.routes.SubGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SessionDestinationHandler(
    private val navigationStateRepository: NavigationStateRepository,
    private val seravianSessionRepository: SessionRepository
) {
    fun collectSessionDestinations() {
        CoroutineScope(Dispatchers.Main).launch {
            seravianSessionRepository.sessionDestination.collectLatest { wantedDestination ->
                handleSessionStates(wantedDestination)
            }
        }
    }

    private fun handleSessionStates(wantedDestination: SessionDestinations) {
        when (wantedDestination) {
            SessionDestinations.INITIALIZE -> {

            }

            SessionDestinations.AUTH -> {
                navigationStateRepository.navigate(ClearBackStack(SubGraph.Auth))
            }

            SessionDestinations.ONBOARDING -> {
                navigationStateRepository.navigate(ClearBackStack(SubGraph.OnBoarding))
            }

            SessionDestinations.PATIENT -> {
                navigationStateRepository.updateAccountType(Role.PATIENT.ordinal)
                navigationStateRepository.navigate(ClearBackStack(SubGraph.Patient))
            }

            SessionDestinations.DOCTOR -> {
                navigationStateRepository.updateAccountType(Role.DOCTOR.ordinal)
                navigationStateRepository.navigate(ClearBackStack(SubGraph.Doctor))
            }
        }
    }
}