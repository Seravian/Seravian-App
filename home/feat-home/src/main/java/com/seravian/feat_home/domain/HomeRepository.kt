package com.seravian.feat_home.domain

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_home.domain.DisordersAdvices
import com.seravian.core_home.domain.QuestionAnswer

interface HomeRepository {
    suspend fun getQuestionAnswers(): NetworkResult<List<QuestionAnswer>, NetworkError>

    suspend fun getDisordersAdvices(): NetworkResult<List<DisordersAdvices>, NetworkError>
}