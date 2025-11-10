package com.prashant.marrowquiz.domain.models

data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val skippedQuestions: Int,
    val longestStreak: Int,
    val scorePercentage: Float
) {
    companion object {
        fun fromQuizState(quizState: QuizState): QuizResult {
            val total = quizState.totalQuestions
            val correct = quizState.correctAnswers
            val skipped = quizState.skippedQuestions
            val wrong = total - correct - skipped
            val percentage = if (total > 0) (correct.toFloat() / total) * 100 else 0f
            
            return QuizResult(
                totalQuestions = total,
                correctAnswers = correct,
                wrongAnswers = wrong,
                skippedQuestions = skipped,
                longestStreak = quizState.longestStreak,
                scorePercentage = percentage
            )
        }
    }
}
