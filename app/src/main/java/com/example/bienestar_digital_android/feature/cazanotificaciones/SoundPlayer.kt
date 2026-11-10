package com.example.bienestar_digital_android.feature.cazanotificaciones

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {
    fun playSound(context: Context, soundResId: Int) {
        try {
            val mediaPlayer = MediaPlayer.create(context, soundResId)
            mediaPlayer.start()

            mediaPlayer.setOnCompletionListener { mp ->
                mp.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}