package com.uan.epilepsyalarm20.domain.models

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.data.local.entities.UserEntity
import com.uan.epilepsyalarm20.data.location.LocationManager
import com.uan.epilepsyalarm20.data.repository.AudioPlayer
import com.uan.epilepsyalarm20.data.repository.EmergencyContactRepository
import com.uan.epilepsyalarm20.data.repository.MessageRepository
import com.uan.epilepsyalarm20.data.repository.PreferencesManager
import com.uan.epilepsyalarm20.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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
    private val emergencyRepository: EmergencyContactRepository,
    private val preferencesManager: PreferencesManager,
    private val audioPlayer: AudioPlayer,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _mapsLink = MutableStateFlow<String?>(null)
    val mapsLink: StateFlow<String?> = _mapsLink.asStateFlow()

    private val _emergencyContacts = MutableStateFlow<List<EmergencyContactEntity>>(emptyList())
    private val emergencyContacts: StateFlow<List<EmergencyContactEntity>> = _emergencyContacts.asStateFlow()

    private val _user = MutableStateFlow<UserEntity?>(null)
    private val user: StateFlow<UserEntity?> = _user.asStateFlow()

    private val _isAnyEmergencyContact = MutableStateFlow(false)
    val isAnyEmergencyContact: StateFlow<Boolean> = _isAnyEmergencyContact.asStateFlow()

    private val _countdown = MutableStateFlow(5) // Contador de 5 segundos
    val countdown: StateFlow<Int> = _countdown.asStateFlow()

    private val _isCancelled = MutableStateFlow(false)

    private val _isEmergencyActive = MutableStateFlow(false)
    val isEmergencyActive: StateFlow<Boolean> = _isEmergencyActive.asStateFlow()

    private var torchJob: Job? = null // Para manejar la linterna

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
            launch {
                _isAnyEmergencyContact.value = preferencesManager.getIsAnyContact()
            }
        }
    }

    fun startEmergencyCountdown() {
        _isCancelled.value = false
        _countdown.value = 5
        _isEmergencyActive.value = false

        viewModelScope.launch {
            for (i in 5 downTo 1) {
                delay(1000L)
                if (_isCancelled.value) {
                    _countdown.value = 5
                    return@launch
                }
                _countdown.value = i
            }
            if (!_isCancelled.value) {
                _isEmergencyActive.value = true
                onActivateEmergency()
            }
        }
    }

    fun cancelEmergency() {
        _isCancelled.value = true
        _countdown.value = 5
        _isEmergencyActive.value = false
        audioPlayer.stopAudio()
        stopTorchFlashing()
    }

    @SuppressLint("MissingPermission")
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

    @SuppressLint("MissingPermission")
    private fun sendMessage(phoneNumber: String, emergencyMessage: String, location: String) {
        messageRepository.sendSms("+57$phoneNumber", emergencyMessage, location)
    }

    @SuppressLint("MissingPermission")
    fun onActivateEmergency() {
        viewModelScope.launch {
            val locationDeferred = async { fetchLocation() }
            val location = locationDeferred.await()
            val soundFile = preferencesManager.getSoundPreference() ?: "alarm_one.mp3"
            _mapsLink.value = location

            val userEntity = user.value
            val contacts = emergencyContacts.value

            _isEmergencyActive.value = true
            audioPlayer.playAudio(soundFile, true)
            startTorchFlashing()

            if (userEntity != null && _isAnyEmergencyContact.value) {
                val message = userEntity.mensajeEmergencia
                contacts.forEach { contact ->
                    sendMessage(contact.phoneNumber, message ?: "Ayuda, tengo una emergencia", location)
                }
            }
        }
    }

    // Encender y apagar la linterna constantemente
    @SuppressLint("ServiceCast")
    private fun startTorchFlashing() {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList.firstOrNull() ?: return

        torchJob?.cancel()
        torchJob = viewModelScope.launch {
            while (_isEmergencyActive.value) {
                cameraManager.setTorchMode(cameraId, true) // Encender linterna
                delay(500)
                cameraManager.setTorchMode(cameraId, false) // Apagar linterna
                delay(500)
            }
        }
    }

    private fun stopTorchFlashing() {
        torchJob?.cancel()
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList.firstOrNull() ?: return
        cameraManager.setTorchMode(cameraId, false) // Apagar linterna al cancelar emergencia
    }
}
