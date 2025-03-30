package com.uan.epilepsyalarm20.data.repository

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Singleton
class PreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    private val _initialConfigCompleted = MutableStateFlow(isInitialConfigCompleted())
    val initialConfigCompleted: StateFlow<Boolean> get() = _initialConfigCompleted

    private val _emergencyMethodFlow = MutableStateFlow(getEmergencyMethod() ?: "two_touch")
    val emergencyMethodFlow: StateFlow<String> get() = _emergencyMethodFlow

    // Guardar y recuperar el sonido seleccionado
    fun saveSoundPreference(sound: String) {
        sharedPreferences.edit { putString("selected_sound", sound) }
    }
    fun getSoundPreference(): String? {
        return sharedPreferences.getString("selected_sound", "alarm_one.mp3") // Valor por defecto
    }

    // Desactivar y verificar si se desea mostrar popup
    fun setPopupShown(shown: Boolean) {
        sharedPreferences.edit { putBoolean("popup_shown", shown) }
    }
    fun isPopupShown(): Boolean {
        return sharedPreferences.getBoolean("popup_shown", true) // Por defecto, la primera vez es mostrado
    }

    // Guardar y recuperar método de emergencia
    fun saveEmergencyMethod(method: String) {
        sharedPreferences.edit { putString("emergency_method", method) }
        _emergencyMethodFlow.value = method
    }
    fun getEmergencyMethod(): String? {
        return sharedPreferences.getString("emergency_method", "two_touch")
    }
    fun isEmergencyMethodSaved(): Boolean {
        return sharedPreferences.contains("emergency_method")
    }

    // Guardar y verificar si el usuario ya completó la configuración inicial
    fun setInitialConfigCompleted(completed: Boolean) {
        sharedPreferences.edit { putBoolean("initial_config_completed", completed) }
        _initialConfigCompleted.value = completed // Notificar cambio
    }
    fun isInitialConfigCompleted(): Boolean {
        return sharedPreferences.getBoolean("initial_config_completed", false)
    }

    // Limite de los contactos de emergencia
    fun saveContactCount(count: Int) {
        sharedPreferences.edit { putInt("contact_count", count) } // Cambio de clave
    }

    fun getContactCount(): Int {
        return sharedPreferences.getInt("contact_count", 0) // Clave corregida
    }

//    fun getContactLimit(): Int {
//        return sharedPreferences.getInt("contact_limit", 5) // Valor por defecto
//    }

    // Para tener en cuenta si hay o no contactos de emergencia
    fun saveIsAnyContact() {
        sharedPreferences.edit { putBoolean("there_any_contacts", true) }
    }
    fun allContactsDeleted() {
        sharedPreferences.edit { putBoolean("there_any_contacts", false) }
    }
    fun getIsAnyContact() : Boolean {
        return sharedPreferences.getBoolean("there_any_contacts", false)
    }


}
