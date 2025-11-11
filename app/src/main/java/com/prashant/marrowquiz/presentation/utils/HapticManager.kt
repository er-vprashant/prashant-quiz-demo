package com.prashant.marrowquiz.presentation.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
    
    private var isEnabled = true
    
    fun lightTap() {
        if (!isEnabled || vibrator?.hasVibrator() != true) return

        vibrator?.vibrate(
            VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }
    
    fun successFeedback() {
        if (!isEnabled || vibrator?.hasVibrator() != true) return

        val pattern = longArrayOf(0, 100, 50, 100)
        val amplitudes = intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator?.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
    }
    
    fun errorFeedback() {
        if (!isEnabled || vibrator?.hasVibrator() != true) return

        vibrator?.vibrate(
            VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }
    
    fun warningFeedback() {
        if (!isEnabled || vibrator?.hasVibrator() != true) return

        val pattern = longArrayOf(0, 50, 30, 50, 30, 50)
        val amplitudes = intArrayOf(0, 100, 0, 100, 0, 100)
        vibrator?.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
    }
    
    fun celebrationFeedback() {
        if (!isEnabled || vibrator?.hasVibrator() != true) return

        val pattern = longArrayOf(0, 100, 50, 150, 50, 100, 50, 200)
        val amplitudes = intArrayOf(0, 150, 0, 200, 0, 150, 0, 255)
        vibrator?.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
    }
    
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }
}
