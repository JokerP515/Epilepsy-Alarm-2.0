package com.uan.epilepsyalarm20.data.messaging

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import android.util.Log
import javax.inject.Inject

// Compatibilidad con API 31+ junto a doble SIM
class SmsSender @Inject constructor(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun sendSms(phoneNumber: String, message: String) {
        val subscriptionManager = context.getSystemService(SubscriptionManager::class.java)
        val activeSubscriptionInfoList = subscriptionManager.activeSubscriptionInfoList

        if (activeSubscriptionInfoList.isNullOrEmpty()) {
            Log.e("SmsSender", "No active SIM card found")
            return
        }

        // Obtener la SIM predeterminada para SMS
        val defaultSubscriptionId = SubscriptionManager.getDefaultSmsSubscriptionId()

        // Si no hay SIM predeterminada, usar la primera activa
        val subscriptionId = if (defaultSubscriptionId != SubscriptionManager.INVALID_SUBSCRIPTION_ID) {
            defaultSubscriptionId
        } else {
            activeSubscriptionInfoList[0].subscriptionId
        }

        // Obtener el SmsManager correcto
        val smsManager = context.getSystemService(SmsManager::class.java)!!.createForSubscriptionId(subscriptionId)

        smsManager.sendTextMessage(phoneNumber, null, message, null, null)

        Log.d("SmsSender", "SMS enviado a $phoneNumber usando SIM con ID: $subscriptionId, mensaje: $message")
    }
}
