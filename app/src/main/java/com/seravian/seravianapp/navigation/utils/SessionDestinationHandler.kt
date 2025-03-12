package com.seravian.seravianapp.navigation.utils

import com.greenvenom.core_navigation.data.NavigationType
import com.greenvenom.core_navigation.data.repository.NavigationStateRepository
import com.seravian.feat_network.data.remote.repository.SessionDestinations
import com.seravian.feat_network.data.remote.repository.SessionStateRepository
import com.seravian.feat_navigation.routes.SubGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SessionDestinationHandler(
    private val navigationStateRepository: NavigationStateRepository,
    private val sessionStateRepository: SessionStateRepository
) {
    init {
        collectSessionDestinations()
    }

    fun collectSessionDestinations() {
        CoroutineScope(Dispatchers.Main).launch {
            sessionStateRepository.userSessionDestination.collect { wantedDestination ->
                handleSessionStates(wantedDestination)
            }
        }
    }

    private fun handleSessionStates(wantedDestination: SessionDestinations) {
        when (wantedDestination) {
            SessionDestinations.INITIALIZE -> {

            }
            SessionDestinations.AUTH -> {
                navigationStateRepository.navigate(NavigationType.ClearBackStack(SubGraph.Auth))
            }
            SessionDestinations.ONBOARDING -> {
                navigationStateRepository.navigate(NavigationType.ClearBackStack(SubGraph.OnBoarding))
            }
            SessionDestinations.MAIN -> {
                navigationStateRepository.navigate(NavigationType.ClearBackStack(SubGraph.Main))
            }
        }
    }
}