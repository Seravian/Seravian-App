package com.seravian.feat_home.presentation.viewmodel

import com.seravian.core_home.domain.DisordersAdvices
import com.seravian.core_home.domain.QuestionAnswer

data class HomeState(
    val disordersAdvices: List<DisordersAdvices> = emptyList(),
    val questionAnswers: List<QuestionAnswer> = emptyList()
)
