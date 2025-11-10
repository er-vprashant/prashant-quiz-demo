package com.prashant.marrowquiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prashant.marrowquiz.domain.models.Question
import com.prashant.marrowquiz.domain.models.QuizState
import com.prashant.marrowquiz.domain.usecase.AnswerQuestionUseCase
import com.prashant.marrowquiz.domain.usecase.ResetQuizUseCase
import com.prashant.marrowquiz.domain.usecase.SkipQuestionUseCase
import com.prashant.marrowquiz.presentation.models.QuizUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val answerQuestionUseCase: AnswerQuestionUseCase,
    private val skipQuestionUseCase: SkipQuestionUseCase,
    private val resetQuizUseCase: ResetQuizUseCase
) : ViewModel() {
    
    private var quizState = QuizState()
    
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    
    fun initializeQuiz(questions: List<Question>) {
        quizState = resetQuizUseCase(questions)
        updateUiState()
    }
    
    fun selectAnswer(optionIndex: Int) {
        if (_uiState.value.isAnswerRevealed) return
        
        val currentQuestion = quizState.currentQuestion ?: return
        val isCorrect = optionIndex == currentQuestion.correctOptionIndex
        
        _uiState.value = _uiState.value.copy(
            isAnswerRevealed = true,
            selectedOptionIndex = optionIndex,
            isCorrect = isCorrect
        )
        
        quizState = answerQuestionUseCase(quizState, optionIndex)
        
        viewModelScope.launch {
            delay(2000)
            if (quizState.isQuizCompleted) {
            } else {
                updateUiState()
            }
        }
    }
    
    fun skipQuestion() {
        if (_uiState.value.isAnswerRevealed) return
        
        quizState = skipQuestionUseCase(quizState)
        
        if (quizState.isQuizCompleted) {
            // TODO
        } else {
            updateUiState()
        }
    }
    
    fun restartQuiz() {
        if (quizState.questions.isNotEmpty()) {
            quizState = resetQuizUseCase(quizState.questions)
            updateUiState()
        }
    }
    
    fun getQuizState(): QuizState = quizState
    
    private fun updateUiState() {
        _uiState.value = QuizUiState(
            currentQuestion = quizState.currentQuestion,
            currentQuestionIndex = quizState.currentQuestionIndex,
            totalQuestions = quizState.totalQuestions,
            progress = quizState.progress,
            correctAnswers = quizState.correctAnswers,
            currentStreak = quizState.currentStreak,
            longestStreak = quizState.longestStreak,
            isAnswerRevealed = false,
            selectedOptionIndex = null,
            isCorrect = null,
            showStreakBadge = quizState.currentStreak >= 3
        )
    }
}
