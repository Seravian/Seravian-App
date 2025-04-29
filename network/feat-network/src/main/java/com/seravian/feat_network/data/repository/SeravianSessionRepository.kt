package com.seravian.feat_network.data.repository

import com.greenvenom.core_network.data.ErrorType
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.domain.SessionDestinations
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.greenvenom.core_network.domain.repository.SessionRepository
import com.greenvenom.core_tokens.domain.repo.TokenDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SeravianSessionRepository(
    private val tokenDataSource: TokenDataSource,
    private val remoteDataSource: RemoteDataSource
): SessionRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _sessionDestination = MutableStateFlow(SessionDestinations.INITIALIZE)
    override val sessionDestination = _sessionDestination
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), SessionDestinations.INITIALIZE)

    override fun collectSessionStatus() {
        scope.launch {
            tokenDataSource.getStoredTokensFlow().collect { tokens ->
                when {
                    tokens == null -> _sessionDestination.update { SessionDestinations.AUTH }
                    tokens.refreshToken.isNullOrEmpty() -> _sessionDestination.update { SessionDestinations.ONBOARDING }
                    else -> {
                        if (tokens.isAccessExpired()) {
                            val tokensResponse = remoteDataSource.refreshTokens(tokens.toRefreshTokenRequest())
                            tokensResponse
                                .onSuccess {
                                    tokenDataSource.saveTokensLocally(it.extractTokens())
                                }
                                .onError { error ->
                                    if (error.errorType == ErrorType.BAD_REQUEST) {
                                        tokenDataSource.deleteTokens()
                                    }
                                }
                        } else {
                            _sessionDestination.update { SessionDestinations.MAIN }
                        }
                    }
                }
            }
        }
    }
}