package com.uan.epilepsyalarm20.data.repository

import android.Manifest
import androidx.annotation.RequiresPermission
import com.uan.epilepsyalarm20.data.messaging.SmsSender
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val smsSender: SmsSender,
    //private val whatsAppSender: WhatsAppSender
) {
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun sendSms(phoneNumber: String, message: String, location: String) {
        smsSender.sendSms(phoneNumber, message, location)
    }

//    fun sendWhatsAppMessage(phoneNumber: String, message: String) {
//        whatsAppSender.sendWhatsAppMessage(phoneNumber, message)
//    }
}
