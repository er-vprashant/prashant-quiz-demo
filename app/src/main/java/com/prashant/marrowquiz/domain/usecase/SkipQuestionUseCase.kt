package com.prashant.marrowquiz.domain.usecase

import com.prashant.marrowquiz.domain.models.QuizState
import javax.inject.Inject

class SkipQuestionUseCase @Inject constructor() {
    
    operator fun invoke(currentState: QuizState): QuizState {
        val currentQuestion = currentState.currentQuestion
            ?: return currentState
        
        val newSkippedQuestions = currentState.skippedQuestions + currentState.currentQuestionIndex

        val currentTime = System.currentTimeMillis()
        val timeTaken = currentTime - currentState.questionStartTime
        val newAnswerTimes = currentState.answerTimes + (currentState.currentQuestionIndex to timeTaken)

        val newCurrentStreak = 0

        val nextQuestionIndex = currentState.currentQuestionIndex + 1
        val isQuizCompleted = nextQuestionIndex >= currentState.totalQuestions
        
        return currentState.copy(
            currentQuestionIndex = nextQuestionIndex,
            skippedQuestions = newSkippedQuestions,
            answerTimes = newAnswerTimes,
            currentStreak = newCurrentStreak,
            isQuizCompleted = isQuizCompleted,
            questionStartTime = if (isQuizCompleted) currentState.questionStartTime else currentTime
        )
    }
}
