package com.prashant.marrowquiz.domain.usecase

import com.prashant.marrowquiz.domain.models.QuizState
import javax.inject.Inject

class AnswerQuestionUseCase @Inject constructor() {
    
    operator fun invoke(
        currentState: QuizState,
        selectedOptionIndex: Int
    ): QuizState {
        val currentQuestion = currentState.currentQuestion ?: return currentState
        val isCorrect = selectedOptionIndex == currentQuestion.correctOptionIndex

        val currentTime = System.currentTimeMillis()
        val timeTaken = currentTime - currentState.questionStartTime
        
        val newAnswers = currentState.answers + (currentState.currentQuestionIndex to selectedOptionIndex)
        val newAnswerTimes = currentState.answerTimes + (currentState.currentQuestionIndex to timeTaken)
        val newCorrectAnswers = if (isCorrect) currentState.correctAnswers + 1 else currentState.correctAnswers

        val newCurrentStreak = if (isCorrect) currentState.currentStreak + 1 else 0
        val newLongestStreak = maxOf(currentState.longestStreak, newCurrentStreak)

        val nextQuestionIndex = currentState.currentQuestionIndex + 1
        val isQuizCompleted = nextQuestionIndex >= currentState.totalQuestions
        
        return currentState.copy(
            currentQuestionIndex = nextQuestionIndex,
            answers = newAnswers,
            answerTimes = newAnswerTimes,
            correctAnswers = newCorrectAnswers,
            currentStreak = newCurrentStreak,
            longestStreak = newLongestStreak,
            isQuizCompleted = isQuizCompleted,
            questionStartTime = if (isQuizCompleted) currentState.questionStartTime else currentTime
        )
    }
}
