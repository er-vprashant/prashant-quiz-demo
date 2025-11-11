# MarrowQuiz - Interactive Quiz Application

A modern, feature-rich Android quiz application built with **Jetpack Compose** and **Material Design 3**. Test your knowledge with timed questions, celebration animations, and intelligent answer shuffling.

## Features

### Core Quiz Experience
- **Timed Questions** - 30-second countdown per question with visual progress
- **Multiple Choice** - Clean, intuitive answer selection interface
- **Smart Shuffling** - Answer options randomized to prevent pattern memorization
- **Skip Functionality** - Option to skip difficult questions
- **Real-time Scoring** - Live tracking of correct/incorrect answers

### Visual & Audio Feedback
- **Celebration Animations** - Bouncy button animations and confetti for correct answers
- **Audio Feedback** - Timer ticking sounds and haptic feedback
- **Material Design 3** - Modern, accessible UI with smooth transitions
- **Custom App Icon** - Quiz-themed adaptive icon with splash screen
- **Progress Indicators** - Visual progress bars and question counters

### Smart Features
- **Background Handling** - Timer pauses when app is backgrounded
- **Lifecycle Aware** - Proper state management across app lifecycle
- **Option Shuffling** - Intelligent randomization of answer positions
- **Configurable Settings** - Easy-to-modify quiz behavior via QuizConfig

## Architecture

### Clean Architecture
```
├── Presentation Layer (UI)
│   ├── Screens (Compose UI)
│   ├── ViewModels (State Management)
│   └── Navigation (Compose Navigation)
├── Domain Layer (Business Logic)
│   ├── Models (Data Classes)
│   ├── Use Cases (Business Rules)
│   └── Repository Interfaces
└── Data Layer (Data Sources)
    ├── API Services (Network)
    ├── Repository Implementations
    └── DTOs (Data Transfer Objects)
```

## Tech Stack

### Core Technologies
- **Language**: Kotlin 1.9.0+
- **Platform**: Android SDK 24+ (API Level 24)
- **Build System**: Gradle with Kotlin DSL
- **IDE**: Android Studio Hedgehog+

### Architecture & Design Patterns
- **Architecture**: Clean Architecture (3-layer: Domain/Data/Presentation)
- **Design Pattern**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt (Dagger-based)
- **State Management**: StateFlow & Compose State
- **Configuration**: Centralized QuizConfig object

### UI & User Experience
- **UI Framework**: Jetpack Compose (Latest Stable)
- **Design System**: Material Design 3 (Material You)
- **Navigation**: Jetpack Navigation Compose
- **Animations**: Compose Animation APIs with Spring Physics
- **Theming**: Dynamic color system with light/dark mode
- **Splash Screen**: Android 12+ Splash Screen API
- **Icons**: Material Icons + Custom Vector Drawables

### Networking & Data
- **HTTP Client**: Ktor Client for Android
- **Serialization**: Kotlinx Serialization (JSON)
- **Data Source**: REST API (GitHub Gist)
- **Caching**: In-memory with StateFlow
- **Error Handling**: Comprehensive exception handling

### Audio & Haptics
- **Audio Framework**: Android MediaPlayer + ToneGenerator
- **Sound Effects**: System tones for UI feedback
- **Haptic Feedback**: VibrationEffect API with custom patterns
- **Background Handling**: Lifecycle-aware audio management

### Development & Testing
- **Version Control**: Git
- **Dependency Management**: Gradle Version Catalogs (libs.versions.toml)
- **Code Generation**: Kapt (Kotlin Annotation Processing)
- **Testing**: JUnit 4, Espresso, Compose Testing
- **Code Quality**: Android Lint with custom rules

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+ (Android 7.0)
- Kotlin 1.9.0+

### Installation
1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/marrowquiz.git
   cd marrowquiz
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or use Android Studio's build button

## How to Use

### Playing a Quiz
1. **Launch the app** - Branded splash screen appears
2. **Loading screen** - Questions are fetched from the API
3. **Answer questions** - Tap your choice within 30 seconds
4. **See results** - Celebration animations for correct answers
5. **View final score** - Complete quiz statistics and performance

### Configuration
Modify quiz behavior in `QuizConfig.kt`:
```kotlin
object QuizConfig {
    const val ENABLE_OPTION_SHUFFLING = true
    const val ENABLE_SHUFFLE_LOGGING = true
    const val QUESTION_TIME_LIMIT = 30    
    const val TIMER_WARNING_THRESHOLD = 10
    const val HAPTIC_WARNING_THRESHOLD = 5
}
```

## Key Components

### Screens
- **LoadingScreen** - Fetches questions with animated loading
- **QuizScreen** - Main quiz interface with timer and animations
- **ResultsScreen** - Final score and statistics display

### Core Features

#### Option Shuffling
```kotlin

private fun shuffleQuestionOptions(question: Question): Question {
    val optionsWithIndices = question.options.mapIndexed { index, option ->
        IndexedOption(index, option)
    }
    val shuffledOptions = optionsWithIndices.shuffled()
    val newCorrectIndex = shuffledOptions.indexOfFirst { 
        it.originalIndex == question.correctOptionIndex 
    }
    return question.copy(
        options = shuffledOptions.map { it.option },
        correctOptionIndex = newCorrectIndex
    )
}
```

#### Celebration Animations
- **Scale Animation** - Bouncy button effect for correct answers
- **Confetti System** - Particle effects with physics simulation
- **Animated Checkmarks** - Smooth entrance animations for feedback icons

#### Lifecycle Management
- **Background Pause** - Timer automatically pauses when app is backgrounded
- **Foreground Resume** - Seamlessly continues from where it left off
- **Audio Control** - No sounds when app is not in focus

## UI/UX Highlights

### Visual Design
- **Material Design 3** - Modern color system and typography
- **Adaptive Icons** - Works with all Android launcher shapes
- **Smooth Animations** - 60fps animations with spring physics
- **Accessibility** - Proper contrast ratios and touch targets

### Audio Experience
- **Timer Ticking** - Subtle audio cues for time pressure
- **Haptic Feedback** - Tactile responses for interactions
- **Background Silence** - Respects user expectations for background apps

### Progress Tracking
- **Visual Progress Bar** - Shows quiz completion percentage
- **Question Counter** - "Question X of Y" display
- **Time Remaining** - Circular progress indicator
- **Live Scoring** - Real-time correct/incorrect tracking