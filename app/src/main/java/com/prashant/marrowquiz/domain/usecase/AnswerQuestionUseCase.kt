package com.prashant.marrowquiz.domain.usecase

import com.prashant.marrowquiz.domain.models.QuizState
import javax.inject.Inject

class AnswerQuestionUseCase @Inject constructor() {
    
    operator fun invoke(currentState: QuizState, selectedOptionIndex: Int): QuizState {
        val currentQuestion = currentState.currentQuestion
            ?: return currentState
        
        val isCorrect = selectedOptionIndex == currentQuestion.correctOptionIndex
        val newCorrectAnswers = if (isCorrect) currentState.correctAnswers + 1 else currentState.correctAnswers
        
        val newCurrentStreak = if (isCorrect) currentState.currentStreak + 1 else 0
        val newLongestStreak = maxOf(currentState.longestStreak, newCurrentStreak)
        
        val newUserAnswers = currentState.userAnswers.toMutableList().apply {
            // Ensure list is large enough
            while (size <= currentState.currentQuestionIndex) {
                add(null)
            }
            set(currentState.currentQuestionIndex, selectedOptionIndex)
        }
        
        val newQuestionIndex = currentState.currentQuestionIndex + 1
        val isQuizCompleted = newQuestionIndex >= currentState.totalQuestions
        
        return currentState.copy(
            currentQuestionIndex = newQuestionIndex,
            correctAnswers = newCorrectAnswers,
            currentStreak = newCurrentStreak,
            longestStreak = newLongestStreak,
            userAnswers = newUserAnswers,
            isQuizCompleted = isQuizCompleted
        )
    }
}
