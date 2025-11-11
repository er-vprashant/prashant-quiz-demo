package com.prashant.marrowquiz.presentation.utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedbackService @Inject constructor(
    private val soundManager: SoundManager,
    private val hapticManager: HapticManager
) {
    
    fun onCorrectAnswer() {
        soundManager.playCorrectSound()
        hapticManager.successFeedback()
    }
    
    fun onIncorrectAnswer() {
        soundManager.playIncorrectSound()
        hapticManager.errorFeedback()
    }
    
    fun onButtonClick() {
        soundManager.playButtonClickSound()
        hapticManager.lightTap()
    }
    
    fun onTimerWarning() {
        soundManager.playTimerWarningSound()
        hapticManager.warningFeedback()
    }
    
    fun onQuizComplete() {
        soundManager.playQuizCompleteSound()
        hapticManager.celebrationFeedback()
    }
    
    fun onOptionSelect() {
        hapticManager.lightTap()
    }
    
    fun onTimerTick() {
        soundManager.playTimerWarningSound()
    }
    
    fun onTimerHapticWarning() {
        hapticManager.warningFeedback()
    }
    
    fun setSoundEnabled(enabled: Boolean) {
        soundManager.setEnabled(enabled)
    }
    
    fun setHapticEnabled(enabled: Boolean) {
        hapticManager.setEnabled(enabled)
    }
    
    fun release() {
        soundManager.release()
    }
}
