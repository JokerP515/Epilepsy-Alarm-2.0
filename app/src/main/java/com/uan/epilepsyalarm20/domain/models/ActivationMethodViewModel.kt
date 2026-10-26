package com.uan.epilepsyalarm20.domain.models

import androidx.lifecycle.ViewModel
import com.uan.epilepsyalarm20.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivationMethodViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    fun saveActivationMethod(method: String) {
        preferencesManager.saveEmergencyMethod(method)
    }
    fun getActivationMethod(): String = preferencesManager.getEmergencyMethod() ?: "long_press"
}

enum class ActivationMethod(private val displayName: String, private val value: String) {
//    TWO_TOUCH("Presionar el botón de subir volumen 2 veces", "two_touch"),
//    THREE_TOUCH("Presionar el botón de subir volumen 3 veces", "three_touch"),
    LONG_PRESS("Presionar el botón de subir volumen 5 segundos", "long_press");

    override fun toString(): String = displayName
    fun getValue(): String = value

    companion object {
        fun toEnumActivationMethod(value: String): ActivationMethod {
            return ActivationMethod.entries.find { it.value == value } ?: LONG_PRESS
        }
    }
}