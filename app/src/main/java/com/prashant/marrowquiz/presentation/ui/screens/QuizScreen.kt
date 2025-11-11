package com.prashant.marrowquiz.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prashant.marrowquiz.domain.models.Question
import com.prashant.marrowquiz.domain.models.QuizState
import com.prashant.marrowquiz.presentation.models.QuizUiState
import com.prashant.marrowquiz.presentation.ui.components.QuizTimer
import com.prashant.marrowquiz.presentation.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    questions: List<Question>,
    onQuizCompleted: (QuizState) -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(questions) {
        viewModel.initializeQuiz(questions)
    }
    
    LaunchedEffect(uiState.currentQuestionIndex, uiState.totalQuestions) {
        println("QuizScreen: currentIndex=${uiState.currentQuestionIndex}, total=${uiState.totalQuestions}")
        if (uiState.currentQuestionIndex >= uiState.totalQuestions && uiState.totalQuestions > 0) {
            println("QuizScreen: Quiz completed! Navigating to results...")
            onQuizCompleted(viewModel.getQuizState())
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .widthIn(max = 600.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QuizHeader(uiState = uiState)
        
        uiState.currentQuestion?.let { question ->
            QuestionCard(
                question = question,
                uiState = uiState,
                onOptionSelected = { optionIndex ->
                    viewModel.selectAnswer(optionIndex)
                }
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        SkipButton(
            enabled = !uiState.isAnswerRevealed,
            onClick = { viewModel.skipQuestion() }
        )
    }
}

@Composable
private fun QuizHeader(uiState: QuizUiState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question ${uiState.progressText}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = uiState.showStreakBadge,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    StreakBadge(streak = uiState.currentStreak)
                }
                
                QuizTimer(
                    timeRemaining = uiState.timeRemaining,
                    timeLimit = uiState.timeLimit
                )
            }
        }
        
        LinearProgressIndicator(
            progress = uiState.progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
        )
        
        Text(
            text = "Correct: ${uiState.correctAnswers} | Streak: ${uiState.currentStreak}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun StreakBadge(streak: Int) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🔥 $streak",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun QuestionCard(
    question: Question,
    uiState: QuizUiState,
    onOptionSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = question.question,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp
            )
            
            question.options.forEachIndexed { index, option ->
                OptionButton(
                    text = option,
                    index = index,
                    isSelected = uiState.selectedOptionIndex == index,
                    isCorrect = index == question.correctOptionIndex,
                    isRevealed = uiState.isAnswerRevealed,
                    enabled = !uiState.isAnswerRevealed,
                    onClick = { onOptionSelected(index) }
                )
            }
        }
    }
}

@Composable
private fun OptionButton(
    text: String,
    index: Int,
    isSelected: Boolean,
    isCorrect: Boolean,
    isRevealed: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    // Define semantic colors for better accessibility
    val correctColor = Color(0xFF4CAF50) // Material Green
    val incorrectColor = Color(0xFFF44336) // Material Red
    val selectedColor = MaterialTheme.colorScheme.primary
    
    val backgroundColor = when {
        isRevealed && isCorrect -> correctColor.copy(alpha = 0.15f)
        isRevealed && isSelected && !isCorrect -> incorrectColor.copy(alpha = 0.15f)
        isSelected && !isRevealed -> selectedColor.copy(alpha = 0.1f)
        else -> MaterialTheme.colorScheme.surface
    }
    
    val borderColor = when {
        isRevealed && isCorrect -> correctColor
        isRevealed && isSelected && !isCorrect -> incorrectColor
        isSelected && !isRevealed -> selectedColor
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }
    
    val textColor = when {
        isRevealed && isCorrect -> correctColor
        isRevealed && isSelected && !isCorrect -> incorrectColor
        else -> MaterialTheme.colorScheme.onSurface
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = borderColor,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ('A' + index).toString(),
                    color = if (isRevealed && (isCorrect || (isSelected && !isCorrect))) {
                        Color.White
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Text(
                text = text,
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            
            if (isRevealed) {
                Text(
                    text = if (isCorrect) "✓" else if (isSelected) "✗" else "",
                    color = if (isCorrect) correctColor else incorrectColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SkipButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Skip Question")
    }
}
