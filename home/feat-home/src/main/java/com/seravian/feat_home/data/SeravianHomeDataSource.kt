package com.seravian.feat_home.data

import com.seravian.core_network.api.utils.constructUrl
import com.seravian.core_network.api.utils.safeCall
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_home.data.dto.response.DisordersAdvicesResponse
import com.seravian.core_home.data.dto.response.QuestionAnswerResponse
import com.seravian.feat_home.domain.HomeDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class SeravianHomeDataSource(
    private val authorizedHttpClient: HttpClient
): HomeDataSource {
    override suspend fun getDisordersAdvices(): NetworkResult<List<DisordersAdvicesResponse>, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("patient/general-mental-health-disorders-Advices"))
        }
    }

    override suspend fun getQuestionAnswers(): NetworkResult<List<QuestionAnswerResponse>, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("patient/questions-answers"))
        }
    }
}