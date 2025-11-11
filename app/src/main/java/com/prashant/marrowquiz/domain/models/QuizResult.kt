package com.prashant.marrowquiz.domain.models

data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val skippedQuestions: Int,
    val longestStreak: Int,
    val scorePercentage: Float,
    val timeTaken: Long,
    val averageTimePerQuestion: Float,
    val timeBonus: Int,
    val finalScore: Int
) {
    companion object {
        fun fromQuizState(quizState: QuizState): QuizResult {
            val total = quizState.totalQuestions
            val correct = quizState.correctAnswers
            val skipped = quizState.skippedQuestions.size
            val wrong = total - correct - skipped
            val percentage = if (total > 0) (correct.toFloat() / total) * 100 else 0f
            
            val totalTime = System.currentTimeMillis() - quizState.startTime
            val avgTimePerQuestion = if (total > 0) totalTime / 1000f / total else 0f

            val timeBonus = quizState.answerTimes.values.map { timeMs ->
                val timeSeconds = timeMs / 1000f
                when {
                    timeSeconds <= 5 -> 10
                    timeSeconds <= 10 -> 5
                    timeSeconds <= 15 -> 2
                    else -> 0
                }
            }.sum()
            
            val baseScore = correct * 10
            val finalScore = baseScore + timeBonus
            
            return QuizResult(
                totalQuestions = total,
                correctAnswers = correct,
                wrongAnswers = wrong,
                skippedQuestions = skipped,
                longestStreak = quizState.longestStreak,
                scorePercentage = percentage,
                timeTaken = totalTime,
                averageTimePerQuestion = avgTimePerQuestion,
                timeBonus = timeBonus,
                finalScore = finalScore
            )
        }
    }
}
