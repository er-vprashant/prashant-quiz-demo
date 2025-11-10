package com.prashant.marrowquiz.domain.usecase

import com.prashant.marrowquiz.domain.models.Question
import com.prashant.marrowquiz.domain.repository.QuizRepository
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val repository: QuizRepository
) {

    suspend operator fun invoke(): List<Question> {
        val questions = repository.getQuestions()
        
        if (questions.isEmpty()) {
            throw Exception("No questions available")
        }
        
        return questions
    }
}
