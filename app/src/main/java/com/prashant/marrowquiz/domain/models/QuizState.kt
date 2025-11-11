package com.prashant.marrowquiz.domain.models

import com.prashant.marrowquiz.domain.config.QuizConfig

data class QuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val answers: Map<Int, Int> = emptyMap(),
    val skippedQuestions: Set<Int> = emptySet(),
    val correctAnswers: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val startTime: Long = System.currentTimeMillis(),
    val questionStartTime: Long = System.currentTimeMillis(),
    val questionTimeLimit: Int = QuizConfig.QUESTION_TIME_LIMIT,
    val answerTimes: Map<Int, Long> = emptyMap(),
    val isQuizCompleted: Boolean = false
) {
    val totalQuestions: Int get() = questions.size
    val currentQuestion: Question? get() = questions.getOrNull(currentQuestionIndex)
    val progress: Float get() = if (totalQuestions > 0) currentQuestionIndex.toFloat() / totalQuestions else 0f
    val wrongAnswers: Int get() = totalQuestions - correctAnswers - skippedQuestions.size
}
