package com.seravian.feat_home.data

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.seravian.core_home.domain.DisordersAdvices
import com.seravian.core_home.domain.QuestionAnswer
import com.seravian.feat_home.domain.HomeDataSource
import com.seravian.feat_home.domain.HomeRepository

class HomeRepositoryImpl(
    private val homeDataSource: HomeDataSource
): HomeRepository {
    override suspend fun getQuestionAnswers(): NetworkResult<List<QuestionAnswer>, NetworkError> {
        return homeDataSource.getQuestionAnswers().map {
            it.map { questionAnswerResponse ->
                questionAnswerResponse.extractQuestionAnswer()
            }
        }
    }

    override suspend fun getDisordersAdvices(): NetworkResult<List<DisordersAdvices>, NetworkError> {
        return homeDataSource.getDisordersAdvices().map {
            it.map { disordersAdvicesResponse ->
                disordersAdvicesResponse.extractDisordersAdvices()
            }
        }
    }
}