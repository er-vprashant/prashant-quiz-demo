package com.prashant.marrowquiz.domain.usecase

import com.prashant.marrowquiz.domain.models.QuizResult
import com.prashant.marrowquiz.domain.models.QuizState
import javax.inject.Inject

class CalculateResultUseCase @Inject constructor() {
    
    operator fun invoke(quizState: QuizState): QuizResult {
        return QuizResult.fromQuizState(quizState)
    }
}
