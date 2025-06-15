package com.seravian.feat_home.domain

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_home.data.dto.response.DisordersAdvicesResponse
import com.seravian.core_home.data.dto.response.QuestionAnswerResponse

interface HomeDataSource {
    suspend fun getDisordersAdvices(): NetworkResult<List<DisordersAdvicesResponse>, NetworkError>

    suspend fun getQuestionAnswers(): NetworkResult<List<QuestionAnswerResponse>, NetworkError>
}