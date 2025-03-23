package com.uan.epilepsyalarm20.domain.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uan.epilepsyalarm20.data.repository.PreferencesManager
import com.uan.epilepsyalarm20.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    val isPopupShown = preferencesManager.isPopupShown()
    val isEmergencyMethodSaved = preferencesManager.isEmergencyMethodSaved()
    val isInitialConfigCompleted = preferencesManager.isInitialConfigCompleted()
    val initialConfigCompleted: StateFlow<Boolean> = preferencesManager.initialConfigCompleted

    private val _userExists = MutableStateFlow<Boolean?>(null)
    val userExists: StateFlow<Boolean?> = _userExists

    init {
        viewModelScope.launch {
            _userExists.value = userRepository.userExists()
        }
    }

    // Guardar que el usuario ya completó la configuración inicial
    fun setInitialConfigCompleted() {
        preferencesManager.setInitialConfigCompleted(true)
    }
}
