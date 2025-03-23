package com.uan.epilepsyalarm20

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.uan.epilepsyalarm20.domain.models.EmergencyViewModel
import com.uan.epilepsyalarm20.ui.screens.EmergencyScreen
import com.uan.epilepsyalarm20.ui.theme.EpilepsyAlarm20Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmergencyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configuración de la pantalla para que aparezca sobre la pantalla de bloqueo
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setShowWhenLocked(true)
        setTurnScreenOn(true)

        // Mostrar la pantalla de emergencia cuando se active la alarma
        setContent {
            EpilepsyAlarm20Theme {
                val viewModel: EmergencyViewModel = hiltViewModel()
                EmergencyScreen(viewModel = viewModel)
            }
        }
    }
}
