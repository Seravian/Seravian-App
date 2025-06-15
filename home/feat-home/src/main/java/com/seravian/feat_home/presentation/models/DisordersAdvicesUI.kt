package com.seravian.feat_home.presentation.models

import com.seravian.core_home.domain.DisordersAdvices

data class DisordersAdvicesUI(
    val disorder: String,
    val advices: List<String>
)

fun DisordersAdvices.toUI() = DisordersAdvicesUI(
    disorder = disorder,
    advices = advices
)