package com.prashant.marrowquiz.domain.usecase

import com.prashant.marrowquiz.domain.models.Question
import com.prashant.marrowquiz.domain.models.QuizState
import javax.inject.Inject

class ResetQuizUseCase @Inject constructor() {
    
    operator fun invoke(questions: List<Question>): QuizState {
        val currentTime = System.currentTimeMillis()
        return QuizState(
            questions = questions,
            currentQuestionIndex = 0,
            answers = emptyMap(),
            skippedQuestions = emptySet(),
            correctAnswers = 0,
            currentStreak = 0,
            longestStreak = 0,
            startTime = currentTime,
            questionStartTime = currentTime,
            questionTimeLimit = 30,
            answerTimes = emptyMap(),
            isQuizCompleted = false
        )
    }
}
