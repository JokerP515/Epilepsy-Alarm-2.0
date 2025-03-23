package com.uan.epilepsyalarm20.domain.models

import androidx.lifecycle.ViewModel
import com.uan.epilepsyalarm20.data.repository.AudioPlayer
import com.uan.epilepsyalarm20.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SoundSelectViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val audioPlayer: AudioPlayer
) : ViewModel() {
    val isPlaying: StateFlow<Boolean> = audioPlayer.isPlaying

    fun saveSoundPreference(sound: String) {
        preferencesManager.saveSoundPreference(sound)
    }
    fun getSoundPreference(): String? {
        return preferencesManager.getSoundPreference()
    }

    fun togglePlayStop(sound: Sounds) {
        if (isPlaying.value) {
            audioPlayer.stopAudio()
        } else {
            audioPlayer.playAudio(sound.getFileName(), false)
        }
    }

}

enum class Sounds (private val displayName: String, private val fileName: String) {
    SELECTOPTION("Seleccionar alarma",""),
    ALARM1("Alarma 1", "alarm_one.mp3"),
    ALARM2("Alarma 2", "alarm_two.mp3"),
    ALARM3("Alarma 3", "alarm_three.mp3");

    override fun toString(): String = displayName
    fun getFileName(): String = fileName
}