package com.prashant.marrowquiz.data.api

import com.prashant.marrowquiz.domain.models.Question

interface QuizApiService {
    suspend fun getQuestions(): List<Question>
}
