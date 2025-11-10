package com.prashant.marrowquiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.prashant.marrowquiz.domain.models.QuizState
import com.prashant.marrowquiz.domain.usecase.CalculateResultUseCase
import com.prashant.marrowquiz.presentation.models.ResultsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val calculateResultUseCase: CalculateResultUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(
        ResultsUiState(
            result = calculateResultUseCase(QuizState()),
            isLoading = false
        )
    )
    val uiState: StateFlow<ResultsUiState> = _uiState.asStateFlow()
    
    fun calculateResults(quizState: QuizState) {
        val result = calculateResultUseCase(quizState)
        _uiState.value = ResultsUiState(
            result = result,
            isLoading = false
        )
    }
    
    fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }
}
