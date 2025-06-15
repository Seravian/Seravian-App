package com.seravian.feat_home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.seravian.core_network.data.onSuccess
import com.seravian.core_ui.presentation.BaseViewModel
import com.seravian.feat_home.domain.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val homeRepository: HomeRepository
): BaseViewModel() {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        viewModelScope.launch {
            val disordersAdvices = withContext(Dispatchers.IO) {
                homeRepository.getDisordersAdvices()
            }
            disordersAdvices.onSuccess { advices ->
                _homeState.update {
                    it.copy(
                        disordersAdvices = advices.take(10)
                    )
                }
            }

            val questionAnswers = withContext(Dispatchers.IO) {
                homeRepository.getQuestionAnswers()
            }
            questionAnswers.onSuccess { answers ->
                _homeState.update {
                    it.copy(
                        questionAnswers = answers.take(10)
                    )
                }
            }
        }
    }
}