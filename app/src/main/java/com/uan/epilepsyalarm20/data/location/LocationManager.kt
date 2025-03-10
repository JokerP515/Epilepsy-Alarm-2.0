package com.uan.epilepsyalarm20.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Clase usada para obtener la localización actual del usuario
// y crear un enlace a Google Maps con la ubicación actual
// Solo obtiene la ubicación cuando se solicita, por lo que no sigue activa en segundo plano
class LocationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getCurrentLocation(onLocationReceived: (String?) -> Unit) {
        val cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location: Location? ->
            if (location != null) {
                val link = createGoogleMapsLink(location.latitude, location.longitude)
                onLocationReceived(link)
            } else {
                onLocationReceived(null)
            }
        }.addOnFailureListener {
            onLocationReceived(null)
        }
    }

    private fun createGoogleMapsLink(latitude: Double, longitude: Double): String =
        "Mi ubicación actual: maps.google.com/?q=$latitude,$longitude"
}
