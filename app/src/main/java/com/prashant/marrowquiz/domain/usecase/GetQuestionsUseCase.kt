package com.prashant.marrowquiz.domain.usecase

import com.prashant.marrowquiz.domain.models.Question
import com.prashant.marrowquiz.domain.repository.QuizRepository
import com.prashant.marrowquiz.domain.config.QuizConfig
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val repository: QuizRepository
) {

    suspend operator fun invoke(): List<Question> {
        val questions = repository.getQuestions()
        
        if (questions.isEmpty()) {
            throw Exception("No questions available")
        }
        
        return if (QuizConfig.ENABLE_OPTION_SHUFFLING) {
            questions.map { question ->
                shuffleQuestionOptions(question)
            }
        } else {
            questions
        }
    }
    
    private fun shuffleQuestionOptions(question: Question): Question {
        val correctAnswerText = question.options[question.correctOptionIndex]
        val optionsWithIndices = question.options.mapIndexed { index, option ->
            IndexedOption(index, option)
        }
        val shuffledOptions = optionsWithIndices.shuffled()
        val newCorrectIndex = shuffledOptions.indexOfFirst {
            it.originalIndex == question.correctOptionIndex 
        }
        if (QuizConfig.ENABLE_SHUFFLE_LOGGING) {
            println("Question ${question.id}: Correct answer moved from index ${question.correctOptionIndex} to $newCorrectIndex")
            println("Original options: ${question.options}")
            println("Shuffled options: ${shuffledOptions.map { it.option }}")
        }
        return question.copy(
            options = shuffledOptions.map { it.option },
            correctOptionIndex = newCorrectIndex
        )
    }
    
    private data class IndexedOption(
        val originalIndex: Int,
        val option: String
    )
}
