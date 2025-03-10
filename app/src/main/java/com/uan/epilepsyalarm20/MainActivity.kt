package com.uan.epilepsyalarm20

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uan.epilepsyalarm20.ui.screens.EmergencyScreen
import com.uan.epilepsyalarm20.ui.theme.EpilepsyAlarm20Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EpilepsyAlarm20Theme {
                EmergencyScreen()
            }
        }
    }
}