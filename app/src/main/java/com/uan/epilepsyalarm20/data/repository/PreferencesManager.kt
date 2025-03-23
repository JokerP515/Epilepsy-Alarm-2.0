package com.uan.epilepsyalarm20.data.repository

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class PreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    // Guardar y recuperar el sonido seleccionado
    fun saveSoundPreference(sound: String) {
        sharedPreferences.edit() { putString("selected_sound", sound) }
    }
    fun getSoundPreference(): String? {
        return sharedPreferences.getString("selected_sound", "alarm_one.mp3") // Valor por defecto
    }

    // Desactivar y verificar si se desea mostrar popup
    fun setPopupShown(shown: Boolean) {
        sharedPreferences.edit() { putBoolean("popup_shown", shown) }
    }
    fun isPopupShown(): Boolean {
        return sharedPreferences.getBoolean("popup_shown", true) // Por defecto, la primera vez es mostrado
    }

    // Guardar y recuperar método de emergencia
    fun saveEmergencyMethod(method: String) {
        sharedPreferences.edit() { putString("emergency_method", method) }
    }
    fun isEmergencyMethodSaved(): Boolean {
        return sharedPreferences.contains("emergency_method")
    }
    fun getEmergencyMethod(): String? {
        return sharedPreferences.getString("emergency_method", null)
    }

    // Guardar y verificar si el usuario ya completó la configuración inicial
    fun setInitialConfigCompleted(completed: Boolean) {
        sharedPreferences.edit() { putBoolean("initial_config_completed", completed) }
    }

    fun isInitialConfigCompleted(): Boolean {
        return sharedPreferences.getBoolean("initial_config_completed", false) // Por defecto, no completada
    }

    // Limite de los contactos de emergencia
    fun saveContactCount(count: Int) {
        sharedPreferences.edit() { putInt("contact_count", count) } // Cambio de clave
    }

    fun getContactCount(): Int {
        return sharedPreferences.getInt("contact_count", 0) // Clave corregida
    }

    fun getContactLimit(): Int {
        return sharedPreferences.getInt("contact_limit", 5) // Valor por defecto
    }

}
