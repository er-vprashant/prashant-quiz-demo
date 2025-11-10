package com.prashant.marrowquiz.domain.models

data class QuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val correctAnswers: Int = 0,
    val skippedQuestions: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val userAnswers: List<Int?> = emptyList(), // null = skipped, index = selected option
    val isQuizCompleted: Boolean = false
) {
    val totalQuestions: Int get() = questions.size
    val currentQuestion: Question? get() = questions.getOrNull(currentQuestionIndex)
    val progress: Float get() = if (totalQuestions > 0) currentQuestionIndex.toFloat() / totalQuestions else 0f
    val wrongAnswers: Int get() = totalQuestions - correctAnswers - skippedQuestions
}
