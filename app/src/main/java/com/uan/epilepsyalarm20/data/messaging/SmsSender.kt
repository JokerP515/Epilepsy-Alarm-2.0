package com.uan.epilepsyalarm20.data.messaging

import android.Manifest
import android.content.Context
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Compatibilidad con API 31+ junto a doble SIM
class SmsSender @Inject constructor(
    @ApplicationContext private val context: Context) {

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun sendSms(phoneNumber: String, message: String, location: String) {
        val subscriptionManager = context.getSystemService(SubscriptionManager::class.java)
        val activeSubscriptionInfoList = subscriptionManager.activeSubscriptionInfoList

        if (activeSubscriptionInfoList.isNullOrEmpty()) {
            Toast.makeText(context, "No se encontró una SIM Card", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener la SIM predeterminada para SMS
        val defaultSubscriptionId = SubscriptionManager.getDefaultSmsSubscriptionId()

        // Si no hay SIM predeterminada, usar la primera activa
        val subscriptionId = if (defaultSubscriptionId != SubscriptionManager.INVALID_SUBSCRIPTION_ID) {
            defaultSubscriptionId
        } else {
            activeSubscriptionInfoList.firstOrNull()?.subscriptionId ?: SubscriptionManager.INVALID_SUBSCRIPTION_ID
        }

        // Verificar si el subscriptionId es válido antes de continuar
        if (subscriptionId == SubscriptionManager.INVALID_SUBSCRIPTION_ID) {
            Toast.makeText(context, "SIM Card no válida", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Obtener el SmsManager correcto
            val smsManager = context.getSystemService(SmsManager::class.java)!!.createForSubscriptionId(subscriptionId)

            // Enviar el mensaje de texto
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            smsManager.sendTextMessage(phoneNumber, null, location, null, null)
        } catch (e: Exception) {
            Log.e("SmsSender", "Error al enviar SMS: ${e.message}")
        }
    }
}
