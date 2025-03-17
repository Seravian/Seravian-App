package com.seravian.feat_network.domain.local

import com.greenvenom.core_network.data.TokenInfo

interface LocalDataSource {
    suspend fun getStoredToken(): TokenInfo?
    suspend fun saveTokenLocally(tokenInfo: TokenInfo)
    suspend fun deleteToken()
}