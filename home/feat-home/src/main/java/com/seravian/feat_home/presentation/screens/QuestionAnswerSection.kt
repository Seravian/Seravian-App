package com.seravian.feat_home.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.seravian.feat_home.R
import com.seravian.feat_home.presentation.components.EmptyStateCard
import com.seravian.feat_home.presentation.components.QuestionAnswerCard
import com.seravian.feat_home.presentation.models.QuestionAnswerUI

@Composable
fun QuestionAnswerSection(
    questionAnswers: List<QuestionAnswerUI>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (questionAnswers.isEmpty()) {
            item {
                EmptyStateCard(
                    title = stringResource(R.string.no_qa_available),
                    subtitle = stringResource(R.string.qa_coming_soon),
                    icon = painterResource(R.drawable.ic_question_answer)
                )
            }
        } else {
            items (items = questionAnswers) { qa ->
                QuestionAnswerCard(questionAnswer = qa)
            }
        }
    }
}