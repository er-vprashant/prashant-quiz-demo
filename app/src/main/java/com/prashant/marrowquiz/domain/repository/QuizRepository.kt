package com.prashant.marrowquiz.domain.repository

import com.prashant.marrowquiz.domain.models.Question

interface QuizRepository {
    /**
     * Fetches quiz questions from the remote API
     * @return List of questions for the quiz
     * @throws Exception if network request fails
     */
    suspend fun getQuestions(): List<Question>
}
