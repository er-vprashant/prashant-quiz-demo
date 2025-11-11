package com.prashant.marrowquiz.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prashant.marrowquiz.domain.models.Question
import com.prashant.marrowquiz.presentation.models.LoadingUiState
import com.prashant.marrowquiz.presentation.viewmodel.LoadingViewModel
import com.valentinilk.shimmer.shimmer

@Composable
fun LoadingScreen(
    onQuestionsLoaded: (List<Question>) -> Unit,
    viewModel: LoadingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoadingUiState.Success -> {
                onQuestionsLoaded(state.questions)
            }
            else -> {}
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is LoadingUiState.Loading -> {
                LoadingContent()
            }
            is LoadingUiState.Error -> {
                ErrorContent(
                    message = state.message,
                    onRetry = { viewModel.retry() }
                )
            }
            is LoadingUiState.Success -> {

            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Marrow Quiz",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Loading questions...",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        val tips = listOf(
            "💡 Tip: Answer quickly to earn bonus points!",
            "⏰ You have 30 seconds per question",
            "🔥 Build streaks for better scores!",
            "🎯 Green means correct, red means wrong"
        )
        
        var currentTipIndex by remember { mutableIntStateOf(0) }
        
        LaunchedEffect(Unit) {
            while (true) {
                delay(1500) // Change tip every 1.5 seconds
                currentTipIndex = (currentTipIndex + 1) % tips.size
            }
        }
        
        Text(
            text = tips[currentTipIndex],
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmer()
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {}
            }
            
            if (index < 2) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Oops! Something went wrong",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = message,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Retry")
        }
    }
}
