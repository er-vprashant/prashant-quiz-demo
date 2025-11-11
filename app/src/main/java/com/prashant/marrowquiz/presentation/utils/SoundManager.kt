package com.prashant.marrowquiz.presentation.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.media.ToneGenerator
import com.prashant.marrowquiz.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var soundPool: SoundPool? = null
    private var toneGenerator: ToneGenerator? = null
    private var correctSoundId: Int = 0
    private var incorrectSoundId: Int = 0
    private var buttonClickSoundId: Int = 0
    private var timerWarningSoundId: Int = 0
    private var quizCompleteSoundId: Int = 0
    
    private var isEnabled = true
    private var useSystemTones = false
    private var soundsLoaded = false
    
    init {
        initializeSoundPool()
        initializeToneGenerator()
    }
    
    private fun initializeSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
            
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        // Set up load complete listener
        var loadedCount = 0
        soundPool?.setOnLoadCompleteListener { _, soundId, status ->
            if (status == 0) {
                loadedCount++
                android.util.Log.d("SoundManager", "Sound loaded successfully: $soundId ($loadedCount/5)")
                if (loadedCount >= 5) {
                    soundsLoaded = true
                    android.util.Log.d("SoundManager", "All sounds loaded successfully")
                }
            } else {
                android.util.Log.e("SoundManager", "Failed to load sound $soundId, status: $status")
                useSystemTones = true
            }
        }

        loadSounds()
    }
    
    private fun initializeToneGenerator() {
        try {
            // Try different audio streams for better compatibility
            toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)
            android.util.Log.d("SoundManager", "ToneGenerator initialized successfully")
        } catch (e: Exception) {
            android.util.Log.e("SoundManager", "Failed to initialize ToneGenerator: ${e.message}")
            toneGenerator = null
        }
    }
    
    private fun loadSounds() {
        soundPool?.let { pool ->
            try {
                android.util.Log.d("SoundManager", "Loading sound files...")
                correctSoundId = pool.load(context, R.raw.correct_answer, 1)
                incorrectSoundId = pool.load(context, R.raw.incorrect_answer, 1)
                buttonClickSoundId = pool.load(context, R.raw.button_click, 1)
                timerWarningSoundId = pool.load(context, R.raw.timer_warning, 1)
                quizCompleteSoundId = pool.load(context, R.raw.quiz_complete, 1)
                
                android.util.Log.d("SoundManager", "Sound IDs - Correct: $correctSoundId, Incorrect: $incorrectSoundId, Click: $buttonClickSoundId")
                
                if (correctSoundId == 0 || incorrectSoundId == 0) {
                    android.util.Log.w("SoundManager", "Some sounds failed to load, using system tones")
                    useSystemTones = true
                }
            } catch (e: Exception) {
                android.util.Log.e("SoundManager", "Exception loading sounds: ${e.message}")
                useSystemTones = true
            }
        }
    }
    
    fun playCorrectSound() {
        if (!isEnabled) return
        
        android.util.Log.d("SoundManager", "Correct sound disabled - no audio feedback")
        // No audio feedback for correct answers - only haptic feedback will play
    }
    
    fun playIncorrectSound() {
        if (!isEnabled) return
        
        android.util.Log.d("SoundManager", "Playing incorrect sound")
        
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 400)
            android.util.Log.d("SoundManager", "Incorrect tone played successfully")
        } catch (e: Exception) {
            android.util.Log.e("SoundManager", "Failed to play incorrect tone: ${e.message}")
        }
    }
    
    fun playButtonClickSound() {
        if (!isEnabled) return
        
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
        } catch (e: Exception) {
            android.util.Log.e("SoundManager", "Failed to play button click: ${e.message}")
        }
    }
    
    fun playTimerWarningSound() {
        if (!isEnabled) return
        
        android.util.Log.d("SoundManager", "Playing clock ticking sound")
        
        try {
            // Use a short, sharp beep to simulate clock ticking
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
        } catch (e: Exception) {
            android.util.Log.e("SoundManager", "Failed to play clock ticking: ${e.message}")
        }
    }
    
    fun playQuizCompleteSound() {
        if (!isEnabled) return
        
        android.util.Log.d("SoundManager", "Quiz complete sound disabled - no audio feedback")
        // No audio feedback for quiz completion - only haptic feedback will play
    }
    
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }
    
    fun testSound() {
        android.util.Log.d("SoundManager", "Testing sound system...")
        android.util.Log.d("SoundManager", "isEnabled: $isEnabled, soundsLoaded: $soundsLoaded, useSystemTones: $useSystemTones")
        android.util.Log.d("SoundManager", "ToneGenerator available: ${toneGenerator != null}")
        android.util.Log.d("SoundManager", "SoundPool available: ${soundPool != null}")
        
        // Check audio manager settings
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
        android.util.Log.d("SoundManager", "Notification volume: $currentVolume/$maxVolume")
        
        // Force play a system tone for testing
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 500)
            android.util.Log.d("SoundManager", "System tone played successfully")
        } catch (e: Exception) {
            android.util.Log.e("SoundManager", "Failed to play system tone: ${e.message}")
        }
    }
    
    fun release() {
        soundPool?.release()
        soundPool = null
        toneGenerator?.release()
        toneGenerator = null
    }
}
