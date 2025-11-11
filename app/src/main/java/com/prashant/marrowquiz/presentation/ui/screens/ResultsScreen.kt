package com.prashant.marrowquiz.presentation.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prashant.marrowquiz.domain.models.QuizState
import com.prashant.marrowquiz.presentation.models.ResultsUiState
import com.prashant.marrowquiz.presentation.viewmodel.ResultsViewModel
import kotlinx.coroutines.delay

@Composable
fun ResultsScreen(
    quizState: QuizState,
    onRestartQuiz: () -> Unit,
    viewModel: ResultsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(quizState) {
        viewModel.calculateResults(quizState)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        ResultsHeader()
        
        ScoreCircle(
            score = uiState.result.scorePercentage,
            correctAnswers = uiState.result.correctAnswers,
            totalQuestions = uiState.result.totalQuestions
        )
        
        StatisticsCards(uiState = uiState)
        
        Spacer(modifier = Modifier.weight(1f))
        
        RestartButton(onClick = onRestartQuiz)
    }
}

@Composable
private fun ResultsHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "🎉",
            fontSize = 48.sp
        )
        
        Text(
            text = "Quiz Complete!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Here's how you did",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ScoreCircle(
    score: Float,
    correctAnswers: Int,
    totalQuestions: Int
) {
    var animatedScore by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(score) {
        val duration = 1500L
        val steps = 60
        val increment = score / steps
        
        repeat(steps) {
            delay(duration / steps)
            animatedScore = minOf(animatedScore + increment, score)
        }
    }
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(200.dp)
    ) {
        CircularProgressIndicator(
            progress = 1f,
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            strokeWidth = 12.dp,
        )
        
        CircularProgressIndicator(
            progress = animatedScore / 100f,
            modifier = Modifier.fillMaxSize(),
            color = when {
                animatedScore >= 80f -> Color(0xFF4CAF50)
                animatedScore >= 60f -> Color(0xFFFF9800)
                else -> Color(0xFFF44336)
            },
            strokeWidth = 12.dp,
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${animatedScore.toInt()}%",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "$correctAnswers/$totalQuestions",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun StatisticsCards(uiState: ResultsUiState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Correct",
                value = uiState.result.correctAnswers.toString(),
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Wrong",
                value = uiState.result.wrongAnswers.toString(),
                color = Color(0xFFF44336),
                modifier = Modifier.weight(1f)
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Skipped",
                value = uiState.result.skippedQuestions.toString(),
                color = Color(0xFFFF9800),
                modifier = Modifier.weight(1f)
            )
            
            StatCard(
                title = "Best Streak",
                value = uiState.result.longestStreak.toString(),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RestartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "Restart Quiz",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
