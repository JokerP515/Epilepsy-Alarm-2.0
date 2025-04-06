package com.uan.epilepsyalarm20.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.uan.epilepsyalarm20.EmergencyActivity
import com.uan.epilepsyalarm20.R
import com.uan.epilepsyalarm20.data.repository.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EmergencyService : Service() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private var powerButtonPressCount = 0
    private var lastPowerPressTime = 0L

    private var _emergencyMethod = "two_touch"
    private var _maxTime = 2500L
    private var _buttonPressCount = 2

    override fun onCreate() {
        super.onCreate()
        observeEmergencyMethodChanges()
        startForegroundServiceWithNotification()
    }

    // Observar cambios en la forma de activación en tiempo de ejecución
    private fun observeEmergencyMethodChanges() {
        CoroutineScope(Dispatchers.IO).launch {
            preferencesManager.emergencyMethodFlow.collect { newMethod ->
                _emergencyMethod = newMethod
                _maxTime = when (_emergencyMethod){
                    "two_touch" -> 2500L
                    "three_touch" -> 4000L
                    else -> 2500L
                }
                _buttonPressCount = when(_emergencyMethod){
                    "two_touch" -> 2
                    "three_touch" -> 3
                    else -> 2
                }
            }
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        listenForKeyPresses()
        return START_STICKY
    }

    //Detecta el encendido/apagado del dispositivo en una ventana de tiempo determinada (5 segundos)
    private fun detectPowerButtonPress() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPowerPressTime < _maxTime) {
            powerButtonPressCount++

            // Depende de la configuración de emergencia del usuario si son 2 o 3 encendidos/apagados
            if(powerButtonPressCount == _buttonPressCount) {
                powerButtonPressCount = 0
                showEmergencyPopup()
            }

            if(powerButtonPressCount > _buttonPressCount) {
                powerButtonPressCount = 0
            }

        } else {
            powerButtonPressCount = 1
        }
        lastPowerPressTime = currentTime
    }

    /**
     * Escucha eventos del encendido/apagado del dispositivo
     */
    private fun listenForKeyPresses() {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_SCREEN_OFF, Intent.ACTION_SCREEN_ON -> {
                        detectPowerButtonPress()
                    }
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        }

        registerReceiver(receiver, filter)
    }

    /**
     * Muestra la pantalla de emergencia
     */
    private fun showEmergencyScreen(context: Context) {
        val intent = Intent(context, EmergencyActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            //preferencesManager.setEmergencyActive(true)
        }
        context.startActivity(intent)
    }

    /**
     * Activa la emergencia y abre la pantalla
     */
    private fun showEmergencyPopup() {
        showEmergencyScreen(this)
    }

    /**
     * Inicia el servicio en primer plano con una notificación
     */
    private fun startForegroundServiceWithNotification() {
        val channelId = "emergency_service_channel"
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(channelId, "Emergency Service", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Detección de Emergencias")
            .setContentText("El servicio está activo en segundo plano.")
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(1, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(1, notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
