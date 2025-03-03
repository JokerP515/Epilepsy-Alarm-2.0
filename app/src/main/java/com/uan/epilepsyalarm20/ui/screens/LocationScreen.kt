package com.uan.epilepsyalarm20.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uan.epilepsyalarm20.data.location.LocationManager
import com.uan.epilepsyalarm20.data.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// Screen de ejemplo para obtener la ubicación
@Composable
fun LocationScreen(viewModel: LocationViewModel = hiltViewModel()) {
    val mapsLink by viewModel.mapsLink.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        mapsLink?.let {
            Text("Ubicación: $it")
        }

        Button(onClick = { viewModel.emergencyButtonOnClick() }) {
            Text("Enviar mensaje")
        }
    }
}

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationManager: LocationManager,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _mapsLink = MutableStateFlow<String?>(null)
    val mapsLink: StateFlow<String?> = _mapsLink.asStateFlow()

    // Obtener la ubicación y esperar su resultado
    private suspend fun fetchLocation(): String? {
        return suspendCoroutine { continuation ->
            locationManager.getCurrentLocation { link ->
                continuation.resume(link)
            }
        }
    }

    private fun sendMessage(location: String) {
        val message = "Emergencia, mi ubicación es esta: $location"
        messageRepository.sendSms("+TESTNUMBER", message)
    }

    fun emergencyButtonOnClick() {
        viewModelScope.launch {
            val location = fetchLocation() ?: "Ubicación no disponible"
            _mapsLink.value = location // Actualiza la UI con la ubicación obtenida
            sendMessage(location) // Ahora el mensaje tiene la ubicación real
        }
        sendMessage(" TEST Ubicación no disponible")
    }
}
