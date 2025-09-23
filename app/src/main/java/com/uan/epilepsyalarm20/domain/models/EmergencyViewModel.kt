package com.uan.epilepsyalarm20.domain.models

import android.Manifest
import android.content.Context
import android.hardware.camera2.CameraManager
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _emergencyContacts = MutableStateFlow<List<EmergencyContactEntity>>(emptyList())
    private val emergencyContacts: StateFlow<List<EmergencyContactEntity>> = _emergencyContacts.asStateFlow()

    private val _user = MutableStateFlow<UserEntity?>(null)
    private val user: StateFlow<UserEntity?> = _user.asStateFlow()

    private val _isAnyEmergencyContact = MutableStateFlow(false)

    private val _countdown = MutableStateFlow(5) // Contador de 5 segundos
    val countdown: StateFlow<Int> = _countdown.asStateFlow()

    private val _isCancelled = MutableStateFlow(false)

    private val _isEmergencyActive = MutableStateFlow(false)
    val isEmergencyActive: StateFlow<Boolean> = _isEmergencyActive.asStateFlow()

    private var torchJob: Job? = null // Para manejar la linterna

    private val _exitEmergencyScreen = MutableSharedFlow<Unit>()
    val exitEmergencyScreen: SharedFlow<Unit> = _exitEmergencyScreen.asSharedFlow()

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

    suspend fun getUser() = userRepository.getUser()

    suspend fun getFirstEmergencyContact() = emergencyRepository.getFirstEmergencyContact()

    fun getUserInstructions(): String? {
        return user.value?.instruccionesEmergencia
    }

    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_PHONE_STATE
    ])
    fun startEmergencyCountdown() {
        _isCancelled.value = false
        _countdown.value = 5
        _isEmergencyActive.value = false

        viewModelScope.launch {
            for (i in 5 downTo 0) {
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

        viewModelScope.launch {
            _exitEmergencyScreen.emit(Unit)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private suspend fun fetchLocation(): String? {
        return withTimeoutOrNull(5000) {
            suspendCoroutine  { continuation ->
                locationManager.getCurrentLocation { link ->
                    continuation.resume(link)
                }
            }
        }
    }


    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS])
    fun sendPreparedMessage(phoneNumber: String, emergencyMessage: String, location: String) {
        messageRepository.sendSms("+57$phoneNumber", emergencyMessage, location)
    }

    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_PHONE_STATE
    ])
    fun onActivateEmergency() {
        val soundFile = preferencesManager.getSoundPreference() ?: "alarm_one.mp3"

        viewModelScope.launch {
            // Verifica si hay contactos de emergencia y procede con el envío del mensaje
            if(_isAnyEmergencyContact.value) {
                val location = fetchLocation()
                _mapsLink.value = location
                val userEntity = user.value
                val contacts = emergencyContacts.value
                _isEmergencyActive.value = true

                if (userEntity != null && location != null) {
                    val message = userEntity.mensajeEmergencia ?: "Ayuda, tengo una emergencia."

                    contacts.map { contact ->
                        sendPreparedMessage(contact.phoneNumber, message, location)
                    }
                }
            }
            audioPlayer.playAudio(soundFile, true)
            startTorchFlashing()
        }
    }


    // Encender y apagar la linterna constantemente
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
