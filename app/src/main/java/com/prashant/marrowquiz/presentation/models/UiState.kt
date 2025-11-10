package com.prashant.marrowquiz.presentation.models

import com.prashant.marrowquiz.domain.models.Question
import com.prashant.marrowquiz.domain.models.QuizResult

sealed class LoadingUiState {
    object Loading : LoadingUiState()
    data class Success(val questions: List<Question>) : LoadingUiState()
    data class Error(val message: String) : LoadingUiState()
}

data class QuizUiState(
    val currentQuestion: Question? = null,
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 0,
    val progress: Float = 0f,
    val correctAnswers: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val isAnswerRevealed: Boolean = false,
    val selectedOptionIndex: Int? = null,
    val isCorrect: Boolean? = null,
    val showStreakBadge: Boolean = false
) {
    val progressText: String get() = "${currentQuestionIndex + 1}/$totalQuestions"
}

data class ResultsUiState(
    val result: QuizResult,
    val isLoading: Boolean = false
)
