package com.uan.epilepsyalarm20

import android.Manifest
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.uan.designsystem.uikit.theme.UanTheme
import com.uan.epilepsyalarm20.domain.models.EmergencyViewModel
import com.uan.epilepsyalarm20.ui.screens.emergency.EmergencyScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmergencyActivity : ComponentActivity() {

    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_PHONE_STATE
    ])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configuración de la pantalla para que aparezca sobre la pantalla de bloqueo
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        setShowWhenLocked(true)
        setTurnScreenOn(true)

        // Mostrar la pantalla de emergencia cuando se active la alarma
        setContent {
            UanTheme {
                val viewModel: EmergencyViewModel = hiltViewModel()
                LaunchedEffect(Unit) {
                    viewModel.exitEmergencyScreen.collect {
                        finish() // Cierra toda la Task relacionada a la emergencia sin terminar el servicio
                    }
                }
                EmergencyScreen(viewModel)
            }
        }
    }
}
