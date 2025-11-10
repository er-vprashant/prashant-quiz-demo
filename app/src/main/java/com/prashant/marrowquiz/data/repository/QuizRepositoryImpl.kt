package com.prashant.marrowquiz.data.repository

import com.prashant.marrowquiz.data.api.QuizApiService
import com.prashant.marrowquiz.domain.models.Question
import com.prashant.marrowquiz.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val apiService: QuizApiService
) : QuizRepository {
    
    override suspend fun getQuestions(): List<Question> {
        return apiService.getQuestions()
    }
}
