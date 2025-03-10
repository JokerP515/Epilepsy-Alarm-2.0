package com.uan.epilepsyalarm20.domain.models

import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.data.local.entities.UserEntity
import com.uan.epilepsyalarm20.data.location.LocationManager
import com.uan.epilepsyalarm20.data.repository.EmergencyContactRepository
import com.uan.epilepsyalarm20.data.repository.MessageRepository
import com.uan.epilepsyalarm20.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class EmergencyViewModel @Inject constructor(
    private val locationManager: LocationManager,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val emergencyRepository: EmergencyContactRepository
) : ViewModel() {

    private val _mapsLink = MutableStateFlow<String?>(null)
    val mapsLink: StateFlow<String?> = _mapsLink.asStateFlow()

    private val _emergencyContacts = MutableStateFlow<List<EmergencyContactEntity>>(emptyList())
    private val emergencyContacts: StateFlow<List<EmergencyContactEntity>> = _emergencyContacts.asStateFlow()

    private val _user = MutableStateFlow<UserEntity?>(null)
    private val user: StateFlow<UserEntity?> = _user.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                userRepository.getUser()?.let { _user.value = it }
            }

            launch {
                emergencyRepository.getEmergencyContacts().collect { contacts ->
                    _emergencyContacts.value = contacts
                }
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private suspend fun fetchLocation(): String {
        return withTimeoutOrNull(5000) {
            suspendCoroutine { continuation ->
                locationManager.getCurrentLocation { link ->
                    Log.d("LocationViewModel", "Ubicación obtenida: $link")
                    continuation.resume(link)
                }
            }
        } ?: "Ubicación no disponible"
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    private fun sendMessage(phoneNumber: String, emergencyMessage: String, location: String) {
        messageRepository.sendSms(phoneNumber, emergencyMessage, location)
    }

    // TEST ONLY
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE])
    fun emergencyButtonOnClick() {
        viewModelScope.launch {
            val locationDeferred = async { fetchLocation() }
            val location = locationDeferred.await()
            _mapsLink.value = location

            val userEntity = user.value
            val contacts = emergencyContacts.value

            if (userEntity != null) {
                val message = userEntity.mensajeEmergencia
                contacts.forEach { contact ->
                    sendMessage(contact.phoneNumber, message, location)
                }
            }
        }
    }
}
