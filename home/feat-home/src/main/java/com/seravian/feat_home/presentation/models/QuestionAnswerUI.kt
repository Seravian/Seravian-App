package com.seravian.feat_home.presentation.models

import com.seravian.core_home.domain.QuestionAnswer

data class QuestionAnswerUI(
    val question: String,
    val answer: String
)

 fun QuestionAnswer.toUI() = QuestionAnswerUI(
     question = question,
     answer = answer
 )