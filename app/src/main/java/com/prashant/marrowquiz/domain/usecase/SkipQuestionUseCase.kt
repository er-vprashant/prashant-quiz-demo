package com.prashant.marrowquiz.domain.usecase

import com.prashant.marrowquiz.domain.models.QuizState
import javax.inject.Inject

class SkipQuestionUseCase @Inject constructor() {
    
    operator fun invoke(currentState: QuizState): QuizState {
        val currentQuestion = currentState.currentQuestion
            ?: return currentState
        
        val newCurrentStreak = 0
        
        val newUserAnswers = currentState.userAnswers.toMutableList().apply {
            while (size <= currentState.currentQuestionIndex) {
                add(null)
            }
            set(currentState.currentQuestionIndex, null)
        }
        
        val newQuestionIndex = currentState.currentQuestionIndex + 1
        val isQuizCompleted = newQuestionIndex >= currentState.totalQuestions
        
        return currentState.copy(
            currentQuestionIndex = newQuestionIndex,
            skippedQuestions = currentState.skippedQuestions + 1,
            currentStreak = newCurrentStreak,
            userAnswers = newUserAnswers,
            isQuizCompleted = isQuizCompleted
        )
    }
}
