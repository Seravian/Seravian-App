package com.seravian.feat_network.data.remote.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionStateRepository() {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _userSessionDestination = MutableStateFlow(SessionDestinations.INITIALIZE)
    val userSessionDestination = _userSessionDestination
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), SessionDestinations.INITIALIZE)

    init {
        collectSessionStatus()
    }

    fun collectSessionStatus() {
        scope.launch {

        }
    }
}