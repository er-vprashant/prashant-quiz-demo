package com.prashant.marrowquiz.domain.usecase

import com.prashant.marrowquiz.domain.models.Question
import com.prashant.marrowquiz.domain.models.QuizState
import javax.inject.Inject

class ResetQuizUseCase @Inject constructor() {
    
    operator fun invoke(questions: List<Question>): QuizState {
        return QuizState(
            questions = questions,
            currentQuestionIndex = 0,
            correctAnswers = 0,
            skippedQuestions = 0,
            currentStreak = 0,
            longestStreak = 0,
            userAnswers = emptyList(),
            isQuizCompleted = false
        )
    }
}
