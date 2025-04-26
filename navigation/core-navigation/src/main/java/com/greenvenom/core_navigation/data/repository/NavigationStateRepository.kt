package com.greenvenom.core_navigation.data.repository

import com.greenvenom.core_navigation.data.NavigationType
import com.greenvenom.core_navigation.domain.DestinationType
import com.greenvenom.core_navigation.utils.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigationStateRepository(private var appNavigator: AppNavigator) {
    private val _navigationState = MutableStateFlow(NavigationState())
    val navigationState = _navigationState.asStateFlow()

    fun navigate(navigationType: NavigationType) {
        when(navigationType) {
            is NavigationType.Back -> appNavigator.navigateBack()
            is NavigationType.ClearBackStack -> appNavigator.navigateAndClearBackStack(navigationType.destination)
            is NavigationType.BottomNavigation -> appNavigator.navigateFromBottomBar(navigationType.destination)
            is NavigationType.Standard -> appNavigator.navigateTo(navigationType.destination)
        }

        updateStoredDestinations()
    }

    fun updateStoredDestinations() {
        _navigationState.update {
            it.copy(
                currentDestination = appNavigator.getCurrentDestination(),
                previousDestination = appNavigator.getPreviousDestination()
            )
        }
        updateBarsState()
    }

    private fun updateBarsState() {
        when (_navigationState.value.currentDestination?.destinationType) {
            DestinationType.MAIN -> {
                _navigationState.update {
                    it.copy(
                        isCurrentDestinationSide = false,
                        bottomBarState = true,
                        topBarState = true
                    )
                }
            }
            DestinationType.SIDE -> {
                _navigationState.update {
                    it.copy(
                        isCurrentDestinationSide = true,
                        bottomBarState = false,
                        topBarState = true
                    )
                }
            }
            else -> {
                _navigationState.update {
                    it.copy(
                        isCurrentDestinationSide = false,
                        bottomBarState = false,
                        topBarState = false
                    )
                }
            }
        }
    }
}