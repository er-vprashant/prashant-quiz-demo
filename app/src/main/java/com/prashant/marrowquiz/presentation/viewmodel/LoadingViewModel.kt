package com.prashant.marrowquiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prashant.marrowquiz.domain.usecase.GetQuestionsUseCase
import com.prashant.marrowquiz.presentation.models.LoadingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<LoadingUiState>(LoadingUiState.Loading)
    val uiState: StateFlow<LoadingUiState> = _uiState.asStateFlow()
    
    init {
        loadQuestions()
    }
    
    private fun loadQuestions() {
        viewModelScope.launch {
            _uiState.value = LoadingUiState.Loading
            
            val startTime = System.currentTimeMillis()
            
            try {
                val questions = getQuestionsUseCase()

                val elapsedTime = System.currentTimeMillis() - startTime
                val minLoadingTime = 2000L

                if (elapsedTime < minLoadingTime) {
                    delay(minLoadingTime - elapsedTime)
                }
                
                _uiState.value = LoadingUiState.Success(questions)
            } catch (e: Exception) {
                val elapsedTime = System.currentTimeMillis() - startTime
                val minLoadingTime = 1500L
                
                if (elapsedTime < minLoadingTime) {
                    delay(minLoadingTime - elapsedTime)
                }
                
                _uiState.value = LoadingUiState.Error(
                    e.message ?: "Failed to load questions"
                )
            }
        }
    }
    
    fun retry() {
        loadQuestions()
    }
}
