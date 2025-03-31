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
    fun getActivationMethod(): String = preferencesManager.getEmergencyMethod() ?: "two_touch"
}

enum class ActivationMethod(private val displayName: String, private val value: String) {
    TWO_TOUCH("Oprimir 2 veces el botón de encendido", "two_touch"),
    THREE_TOUCH("Oprimir 3 veces el botón de encendido", "three_touch");

    override fun toString(): String = displayName
    fun getValue(): String = value

    companion object {
        fun toEnumActivationMethod(value: String): ActivationMethod {
            return ActivationMethod.entries.find { it.value == value } ?: TWO_TOUCH
        }
    }
}