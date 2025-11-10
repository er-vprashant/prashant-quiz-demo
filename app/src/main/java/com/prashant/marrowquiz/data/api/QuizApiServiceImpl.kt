package com.prashant.marrowquiz.data.api

import com.prashant.marrowquiz.domain.models.Question
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class QuizApiServiceImpl @Inject constructor(
    private val httpClient: HttpClient
) : QuizApiService {
    
    companion object {
        private const val QUESTIONS_URL = "https://gist.githubusercontent.com/dr-samrat/53846277a8fcb034e482906ccc0d12b2/raw"
    }
    
    override suspend fun getQuestions(): List<Question> {
        return try {
            httpClient.get(QUESTIONS_URL).body()
        } catch (e: Exception) {
            throw Exception("Failed to fetch questions: ${e.message}", e)
        }
    }
}
