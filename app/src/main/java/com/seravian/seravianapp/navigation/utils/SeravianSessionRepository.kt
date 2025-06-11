package com.seravian.seravianapp.navigation.utils

import com.greenvenom.core_network.data.ErrorType
import com.greenvenom.core_network.data.SessionDestinations
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.domain.repository.SessionRepository
import com.greenvenom.core_tokens.domain.Tokens
import com.greenvenom.core_tokens.domain.repo.TokensDataSource
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.core_profile.domain.utils.Role
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SeravianSessionRepository(
    private val tokensDataSource: TokensDataSource,
    private val roomDataSource: LocalDataSource
): SessionRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _sessionDestination = MutableStateFlow(SessionDestinations.INITIALIZE)
    override val sessionDestination = _sessionDestination
        .stateIn(scope, SharingStarted.Lazily, SessionDestinations.INITIALIZE)

    init {
        collectSessionStatus()
    }

    override fun collectSessionStatus() {
        scope.launch {
            tokensDataSource.getStoredTokensFlow().collect { tokens ->
                when {
                    tokens == Tokens() -> _sessionDestination.update { SessionDestinations.AUTH }
                    tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNullOrEmpty() ->
                        _sessionDestination.update { SessionDestinations.ONBOARDING }
                    else -> {
                        if (tokens.isAccessExpired()) {
                            val tokensResponse = tokensDataSource.refreshTokens(tokens.toRefreshTokenRequest())
                            tokensResponse
                                .onError { error ->
                                    if (error.errorType == ErrorType.BAD_REQUEST) {
                                        tokensDataSource.deleteTokens()
                                    }
                                }
                        } else {
                            val currentProfile = roomDataSource.getProfile().extractProfile()

                            if (currentProfile.role == Role.PATIENT) {
                                _sessionDestination.update { SessionDestinations.PATIENT }
                            } else {
                                _sessionDestination.update { SessionDestinations.DOCTOR }
                            }
                        }
                    }
                }
            }
        }
    }
}