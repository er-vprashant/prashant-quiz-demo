package com.prashant.marrowquiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.prashant.marrowquiz.domain.models.Question
import com.prashant.marrowquiz.domain.models.QuizState
import com.prashant.marrowquiz.domain.usecase.AnswerQuestionUseCase
import com.prashant.marrowquiz.domain.usecase.ResetQuizUseCase
import com.prashant.marrowquiz.domain.usecase.SkipQuestionUseCase
import com.prashant.marrowquiz.presentation.models.QuizUiState
import com.prashant.marrowquiz.presentation.utils.FeedbackService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private val resetQuizUseCase: ResetQuizUseCase,
    private val feedbackService: FeedbackService
) : ViewModel(), DefaultLifecycleObserver {
    
    private var quizState = QuizState()
    private var timerJob: Job? = null
    private var isAppInBackground = false
    private var pausedTimeRemaining = 0
    
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
    
    fun initializeQuiz(questions: List<Question>) {
        quizState = resetQuizUseCase(questions)
        updateUiState()
        startTimer()
    }
    
    fun selectAnswer(optionIndex: Int) {
        if (_uiState.value.isAnswerRevealed) return
        
        stopTimer()
        val currentQuestion = quizState.currentQuestion ?: return
        val isCorrect = optionIndex == currentQuestion.correctOptionIndex

        feedbackService.onOptionSelect()
        
        _uiState.value = _uiState.value.copy(
            isAnswerRevealed = true,
            selectedOptionIndex = optionIndex,
            isCorrect = isCorrect
        )
        
        quizState = answerQuestionUseCase(quizState, optionIndex)

        if (isCorrect) {
            feedbackService.onCorrectAnswer()
        } else {
            feedbackService.onIncorrectAnswer()
        }

        viewModelScope.launch {
            delay(2000)
            if (quizState.isQuizCompleted) {
                feedbackService.onQuizComplete()
                updateUiState()
            } else {
                updateUiState()
                startTimer()
            }
        }
    }
    
    fun skipQuestion() {
        if (_uiState.value.isAnswerRevealed) return
        
        stopTimer()
        quizState = skipQuestionUseCase(quizState)
        
        if (quizState.isQuizCompleted) {
            feedbackService.onQuizComplete()
            updateUiState()
        } else {
            updateUiState()
            startTimer()
        }
    }
    
    fun restartQuiz() {
        if (quizState.questions.isNotEmpty()) {
            stopTimer()
            quizState = resetQuizUseCase(quizState.questions)
            updateUiState()
            startTimer()
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
            showStreakBadge = quizState.currentStreak >= 3,
            timeRemaining = quizState.questionTimeLimit,
            timeLimit = quizState.questionTimeLimit,
            isTimeUp = false
        )
    }
    
    private fun startTimer() {
        stopTimer()
        timerJob = viewModelScope.launch {
            var timeLeft = quizState.questionTimeLimit
            while (timeLeft > 0 && !isAppInBackground) {
                _uiState.value = _uiState.value.copy(timeRemaining = timeLeft)
                if (timeLeft <= 10 && !isAppInBackground) {
                    feedbackService.onTimerTick()
                }

                if (timeLeft == 5 && !isAppInBackground) {
                    feedbackService.onTimerHapticWarning()
                }
                
                delay(1000)
                timeLeft--
            }

            if (!isAppInBackground) {
                _uiState.value = _uiState.value.copy(timeRemaining = 0, isTimeUp = true)
                if (!_uiState.value.isAnswerRevealed) {
                    skipQuestion()
                }
            }
        }
    }
    
    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        isAppInBackground = true
        pausedTimeRemaining = _uiState.value.timeRemaining
        stopTimer()
    }
    
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (isAppInBackground && !_uiState.value.isAnswerRevealed && pausedTimeRemaining > 0) {
            isAppInBackground = false
            startTimerWithTime(pausedTimeRemaining)
        }
        isAppInBackground = false
    }
    
    private fun startTimerWithTime(startTime: Int) {
        stopTimer()
        timerJob = viewModelScope.launch {
            var timeLeft = startTime
            while (timeLeft > 0 && !isAppInBackground) {
                _uiState.value = _uiState.value.copy(timeRemaining = timeLeft)
                if (timeLeft <= 10 && !isAppInBackground) {
                    feedbackService.onTimerTick()
                }

                if (timeLeft == 5 && !isAppInBackground) {
                    feedbackService.onTimerHapticWarning()
                }
                
                delay(1000)
                timeLeft--
            }

            if (!isAppInBackground) {
                _uiState.value = _uiState.value.copy(timeRemaining = 0, isTimeUp = true)
                if (!_uiState.value.isAnswerRevealed) {
                    skipQuestion()
                }
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        feedbackService.release()
    }
}
