package com.prashant.marrowquiz.data.api

import com.prashant.marrowquiz.domain.models.Question
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import javax.inject.Inject

class QuizApiServiceImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val json: Json
) : QuizApiService {
    
    companion object {
        private const val QUESTIONS_URL = "https://gist.githubusercontent.com/dr-samrat/53846277a8fcb034e482906ccc0d12b2/raw"
    }
    
    override suspend fun getQuestions(): List<Question> {
        return try {
            val response = httpClient.get(QUESTIONS_URL)
            try {
                val questions: List<Question> = response.body()
                println("Successfully parsed ${questions.size} questions using automatic deserialization")
                return questions
            } catch (autoParseError: Exception) {
                println("Automatic parsing failed: ${autoParseError.message}")

                val jsonString = response.bodyAsText()
                println("Raw JSON response: ${jsonString.take(200)}...")
                
                val questions = json.decodeFromString<List<Question>>(jsonString)
                println("Successfully parsed ${questions.size} questions using manual parsing")
                return questions
            }
        } catch (e: Exception) {
            println("API Error: ${e::class.simpleName}: ${e.message}")
            e.printStackTrace()
            throw Exception("Failed to fetch questions: ${e.message}", e)
        }
    }
}
