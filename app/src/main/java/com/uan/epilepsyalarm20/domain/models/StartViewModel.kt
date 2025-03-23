package com.uan.epilepsyalarm20.domain.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uan.epilepsyalarm20.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    suspend fun getUser() = userRepository.getUser()

    suspend fun getEmergencyMessage() = userRepository.getEmergencyMessage()
    suspend fun getEmergencyInstructions() = userRepository.getEmergencyInstructions()

    fun updateEmergencyMessage(message: String) {
        viewModelScope.launch {
            userRepository.updateEmergencyMessage(message)
        }
    }

    fun updateEmergencyInstructions(instructions: String) {
        viewModelScope.launch {
            userRepository.updateEmergencyInstructions(instructions)
        }
    }

}