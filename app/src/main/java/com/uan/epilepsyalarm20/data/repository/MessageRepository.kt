package com.uan.epilepsyalarm20.data.repository

import com.uan.epilepsyalarm20.data.messaging.SmsSender
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val smsSender: SmsSender,
    //private val whatsAppSender: WhatsAppSender
) {
    fun sendSms(phoneNumber: String, message: String) {
        smsSender.sendSms(phoneNumber, message)
    }

//    fun sendWhatsAppMessage(phoneNumber: String, message: String) {
//        whatsAppSender.sendWhatsAppMessage(phoneNumber, message)
//    }
}
