package com.uan.epilepsyalarm20.domain.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.data.repository.EmergencyContactRepository
import com.uan.epilepsyalarm20.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: EmergencyContactRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val limit = 5

    private val _currentCount = MutableStateFlow(0)
    val currentCount: StateFlow<Int> = _currentCount

    var emergencyContactsFlow = contactsRepository.getEmergencyContacts()

    init {
        viewModelScope.launch {
            _currentCount.value = preferencesManager.getContactCount()
        }
    }

    fun insertEmergencyContact(name: String, phoneNumber: String): Boolean {
        return if (_currentCount.value < limit) {
            val contact = EmergencyContactEntity(
                userId = 1,
                name = name,
                phoneNumber = phoneNumber
            )
            viewModelScope.launch {
                contactsRepository.insertEmergencyContact(contact)
                val newCount = _currentCount.value + 1
                _currentCount.value = newCount
                preferencesManager.saveContactCount(newCount)
            }
            true
        } else {
            false
        }
    }

    fun deleteEmergencyContact(contact: EmergencyContactEntity) {
        viewModelScope.launch {
            contactsRepository.deleteEmergencyContactById(contact.id)
            val newCount = _currentCount.value - 1
            _currentCount.value = newCount
            preferencesManager.saveContactCount(newCount)
        }
    }

    fun updateEmergencyContact(contact: EmergencyContactEntity) {
        viewModelScope.launch {
            contactsRepository.updateEmergencyContact(contact)
        }
    }
}


/*
*
* */
