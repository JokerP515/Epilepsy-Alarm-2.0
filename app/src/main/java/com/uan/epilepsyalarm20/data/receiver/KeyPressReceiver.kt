package com.uan.epilepsyalarm20.data.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.uan.epilepsyalarm20.data.service.EmergencyService

class KeyPressReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, EmergencyService::class.java)
        context?.startService(serviceIntent)
    }
}
