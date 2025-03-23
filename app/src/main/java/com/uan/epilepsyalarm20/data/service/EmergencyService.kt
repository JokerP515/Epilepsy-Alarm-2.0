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
import android.util.Log
import androidx.core.app.NotificationCompat
import com.uan.epilepsyalarm20.EmergencyActivity
import com.uan.epilepsyalarm20.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmergencyService : Service() {

    private var powerButtonPressCount = 0
    private var lastPowerPressTime = 0L

    override fun onCreate() {
        super.onCreate()
        startForegroundServiceWithNotification()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        listenForKeyPresses()
        return START_STICKY
    }

    /**
     *  Detecta la pulsación del botón de encendido
     *  OJO, POSIBLEMENTE MUY INTRUSIVO SI powerButtonPressCount = 1
     */
    private fun detectPowerButtonPress() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPowerPressTime < 3500) {
            powerButtonPressCount++
            if (powerButtonPressCount in 2..3) {
                showEmergencyPopup()
            }
        } else {
            powerButtonPressCount = 1
        }
        lastPowerPressTime = currentTime
    }

    /**
     * Escucha eventos del botón de encendido
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
        }
        context.startActivity(intent)
    }

    /**
     * Activa la emergencia y abre la pantalla
     */
    private fun showEmergencyPopup() {
        showEmergencyScreen(this)
        Log.d("EmergencyService", "EmergencyActivity iniciada")
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
