package com.uan.epilepsyalarm20.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.uan.epilepsyalarm20.data.location.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// Screen de ejemplo para obtener la ubicación
@Composable
fun LocationScreen(viewModel: LocationViewModel = hiltViewModel()) {
    val mapsLink by viewModel.mapsLink.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = { viewModel.fetchLocation() }) {
            Text("Obtener ubicación")
        }

        mapsLink?.let {
            Text("Ubicación: $it")
        }
    }
}


@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationManager: LocationManager
) : ViewModel() {

    private val _mapsLink = MutableStateFlow<String?>(null) // Estado mutable
    val mapsLink: StateFlow<String?> = _mapsLink.asStateFlow() // Exponer estado inmutable

    fun fetchLocation() {
        locationManager.getCurrentLocation { link ->
            _mapsLink.value = link
        }
    }
}
