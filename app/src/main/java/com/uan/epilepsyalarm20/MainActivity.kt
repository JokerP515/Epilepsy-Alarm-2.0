package com.uan.epilepsyalarm20

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.uan.epilepsyalarm20.data.service.EmergencyService
import com.uan.epilepsyalarm20.domain.models.MainViewModel
import com.uan.epilepsyalarm20.ui.navigation.AppNavigation
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.theme.EpilepsyAlarm20Theme
import com.uan.epilepsyalarm20.ui.screens.LoadingScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requiredPermission = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.SEND_SMS,
        android.Manifest.permission.READ_PHONE_STATE,
        android.Manifest.permission.WAKE_LOCK,
        android.Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EpilepsyAlarm20Theme {
                val mainViewModel: MainViewModel = hiltViewModel()
                val userExistsState by mainViewModel.userExists.collectAsState(initial = null)

                val startDestination = when {
                    mainViewModel.isPopupShown -> Routes.NotificacionInicial
                    userExistsState == null -> null // Pantalla de carga
                    userExistsState == true -> {
                        if (mainViewModel.isInitialConfigCompleted) Routes.MenuPrincipal
                        else Routes.ConfigAlarma
                    }
                    else -> Routes.PerfilUsuario
                }

                startDestination?.let {
                    AppNavigation(startDestination = it)
                } ?: LoadingScreen()

                // Para activar el servicio de emergencia cuando se completa la configuración inicial
                // Verifica permisos necesarios para la emergencia
                LaunchedEffect(mainViewModel.initialConfigCompleted) {
                    requestPermissions()
                    mainViewModel.initialConfigCompleted.collect { isCompleted ->
                        if (isCompleted) {
                            val intent = Intent(this@MainActivity, EmergencyService::class.java)
                            startService(intent)
                            Toast.makeText(this@MainActivity, "Servicio de emergencia iniciado", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    // Solicita los permisos estándar
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                Toast.makeText(this, "Todos los permisos concedidos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Algunos permisos fueron denegados", Toast.LENGTH_SHORT).show()
                requestPermissions()
            }
        }

    // Solicita los permisos estándar y el permiso de superposición por separado
    private fun requestPermissions() {
        val missingPermissions = requiredPermission.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            requestPermissionLauncher.launch(missingPermissions.toTypedArray()) // Solo pide los que faltan
        } else {
            Toast.makeText(this, "Permisos estándar ya concedidos", Toast.LENGTH_SHORT).show()
        }

        // Maneja SYSTEM_ALERT_WINDOW aparte
        if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission()
        }
    }

    // Abre la configuración para `SYSTEM_ALERT_WINDOW`
    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:$packageName".toUri()
        )
        startActivity(intent)
    }
}