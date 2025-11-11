package com.prashant.marrowquiz.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prashant.marrowquiz.domain.models.Question
import com.prashant.marrowquiz.domain.models.QuizState
import com.prashant.marrowquiz.presentation.ui.screens.LoadingScreen
import com.prashant.marrowquiz.presentation.ui.screens.QuizScreen
import com.prashant.marrowquiz.presentation.ui.screens.ResultsScreen

sealed class Screen(val route: String) {
    object Loading : Screen("loading")
    object Quiz : Screen("quiz")
    object Results : Screen("results")
}

@Composable
fun QuizNavigation(
    navController: NavHostController = rememberNavController()
) {
    var questions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var quizState by remember { mutableStateOf(QuizState()) }
    
    NavHost(
        navController = navController,
        startDestination = Screen.Loading.route
    ) {
        composable(Screen.Loading.route) {
            LoadingScreen(
                onQuestionsLoaded = { loadedQuestions ->
                    questions = loadedQuestions
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Quiz.route) {
            QuizScreen(
                questions = questions,
                onQuizCompleted = { finalQuizState ->
                    quizState = finalQuizState
                    navController.navigate(Screen.Results.route)
                }
            )
        }
        
        composable(Screen.Results.route) {
            ResultsScreen(
                quizState = quizState,
                onRestartQuiz = {
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Loading.route)
                    }
                }
            )
        }
    }
}
