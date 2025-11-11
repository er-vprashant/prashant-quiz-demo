package com.prashant.marrowquiz.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizTimer(
    timeRemaining: Int,
    timeLimit: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (timeLimit > 0) timeRemaining.toFloat() / timeLimit else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "timer_progress"
    )
    
    val timerColor = when {
        timeRemaining <= 5 -> Color(0xFFF44336)
        timeRemaining <= 10 -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.primary
    }
    
    Box(
        modifier = modifier.size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(60.dp)) {
            drawCircle(
                color = timerColor.copy(alpha = 0.2f),
                radius = size.minDimension / 2,
                style = Stroke(width = 6.dp.toPx())
            )
        }

        Canvas(modifier = Modifier.size(60.dp)) {
            drawArc(
                color = timerColor,
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                style = Stroke(
                    width = 6.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }

        Text(
            text = timeRemaining.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = timerColor
        )
    }
}
