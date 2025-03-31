package com.uan.epilepsyalarm20.data.repository

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.uan.epilepsyalarm20.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayer @Inject constructor(@ApplicationContext private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow() // Exponer el estado como Flow

    fun playAudio(fileName: String, isEmergency: Boolean) {
        val resId = getRawResId(fileName)
        if (resId == 0) return

        stopAudio() // Detener cualquier reproducción anterior
        if(isEmergency) setMaxVolume()
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            start()
            isLooping = isEmergency
        }
        _isPlaying.value = true
    }

//    fun pauseAudio() {
//        mediaPlayer?.pause()
//        _isPlaying.value = false
//    }

    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
    }

    private fun getRawResId(fileName: String): Int {
        return try {
            val field = R.raw::class.java.getDeclaredField(fileName.removeSuffix(".mp3"))
            field.getInt(null)
        } catch (e: Exception) {
            e.printStackTrace()
            0 // Devuelve 0 si no encuentra el recurso
        }
    }

    private fun setMaxVolume() {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_SHOW_UI)
    }

}
