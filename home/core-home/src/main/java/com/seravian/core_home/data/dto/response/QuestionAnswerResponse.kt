package com.seravian.core_home.data.dto.response

import com.seravian.core_home.domain.QuestionAnswer
import kotlinx.serialization.Serializable

@Serializable
data class QuestionAnswerResponse(
    val id: Int,
    val question: String,
    val answer: String
) {
    fun extractQuestionAnswer(): QuestionAnswer {
        return QuestionAnswer(
            id = id,
            question = question,
            answer = answer
        )
    }
}
