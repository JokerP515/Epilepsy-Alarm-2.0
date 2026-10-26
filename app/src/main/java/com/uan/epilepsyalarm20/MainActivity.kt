package com.uan.epilepsyalarm20

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.uan.epilepsyalarm20.data.service.VolumeButtonAccessibilityService
import com.uan.epilepsyalarm20.domain.models.MainViewModel
import com.uan.epilepsyalarm20.ui.navigation.AppNavigation
import com.uan.epilepsyalarm20.ui.navigation.routes.Routes
import com.uan.epilepsyalarm20.ui.screens.LoadingScreen
import com.uan.designsystem.uikit.theme.UanTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requiredPermission = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.SEND_SMS,
        android.Manifest.permission.READ_PHONE_STATE,
        android.Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val windowInsetsController = androidx.core.view.WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false

        setContent {
            UanTheme {
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

                // Maneja permisos y accesibilidad cuando la configuración inicial se completa
                LaunchedEffect(mainViewModel.initialConfigCompleted) {
                    mainViewModel.initialConfigCompleted.collect { isCompleted ->
                        if (isCompleted) {
                            requestPermissions()
                            // Verifica si el servicio de accesibilidad está activo
                            if (!isAccessibilityServiceEnabled(VolumeButtonAccessibilityService::class.java)) {
                                openAccessibilitySettingsForService(VolumeButtonAccessibilityService::class.java)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Activa el servicio de accesibilidad para usar la alarma",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Servicio de accesibilidad ya activo",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    // Verifica si el servicio de accesibilidad está activo
    private fun isAccessibilityServiceEnabled(service: Class<out AccessibilityService>): Boolean {
        val am = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        val serviceName = ComponentName(this, service).flattenToString()
        return enabledServices?.split(":")?.contains(serviceName) == true
    }

    // Lleva al usuario directo a la configuración específica de tu servicio
    private fun openAccessibilitySettingsForService(service: Class<out AccessibilityService>) {
        try {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                data = "package:$packageName".toUri()
            }
            startActivity(intent)
        } catch (e: Exception) {
            // Si falla, abre la pantalla general de accesibilidad como respaldo
            Log.e("MainActivity", "Error al abrir configuración de accesibilidad", e)
            val fallbackIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(fallbackIntent)
        }
    }

    // Solicita los permisos estándar
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                Toast.makeText(this, "Todos los permisos concedidos", Toast.LENGTH_SHORT).show()
            } else {
                requestPermissions()
            }
        }

    private fun requestPermissions() {
        val missingPermissions = requiredPermission.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            requestPermissionLauncher.launch(missingPermissions.toTypedArray())
        } else {
            Toast.makeText(this, "Permisos estándar ya concedidos", Toast.LENGTH_SHORT).show()
        }

        // Maneja SYSTEM_ALERT_WINDOW aparte
        if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission()
        }
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:$packageName".toUri()
        )
        startActivity(intent)
    }

}
