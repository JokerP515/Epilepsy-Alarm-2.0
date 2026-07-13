package com.uan.epilepsyalarm20.domain.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.data.repository.EmergencyContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: EmergencyContactRepository
) : ViewModel() {

    val limit = 5

    var emergencyContactsFlow = contactsRepository.getEmergencyContacts()

    val currentCount: StateFlow<Int> = emergencyContactsFlow
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun insertEmergencyContact(name: String, phoneNumber: String): Boolean {
        return if (currentCount.value < limit) {
            val contact = EmergencyContactEntity(
                userId = 1,
                name = name,
                phoneNumber = phoneNumber
            )
            viewModelScope.launch {
                contactsRepository.insertEmergencyContact(contact)
            }
            true
        } else {
            false
        }
    }

    fun deleteEmergencyContact(contact: EmergencyContactEntity) {
        viewModelScope.launch {
            contactsRepository.deleteEmergencyContactById(contact.id)
        }
    }

    fun updateEmergencyContact(contact: EmergencyContactEntity) {
        viewModelScope.launch {
            contactsRepository.updateEmergencyContact(contact)
        }
    }
}