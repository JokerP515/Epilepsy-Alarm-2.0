package com.uan.epilepsyalarm20.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.uan.epilepsyalarm20.data.service.EmergencyService

class KeyPressReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_OFF, Intent.ACTION_SCREEN_ON -> {
                val serviceIntent = Intent(context, EmergencyService::class.java)
                context?.startService(serviceIntent)
            }
        }
    }
}
