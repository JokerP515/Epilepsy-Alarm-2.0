package com.uan.epilepsyalarm20.di.modules

import android.content.Context
import com.uan.epilepsyalarm20.data.messaging.SmsSender
import com.uan.epilepsyalarm20.data.repository.MessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MessagingModule {
    @Provides
    fun provideSmsSender(@ApplicationContext context: Context): SmsSender {
        return SmsSender(context)
    }

//    @Provides
//    fun provideWhatsAppSender(@ApplicationContext context: Context): WhatsAppSender {
//        return WhatsAppSender(context)
//    }

    @Provides
    fun provideMessageRepository(
        smsSender: SmsSender,
        //whatsAppSender: WhatsAppSender
    ): MessageRepository {
        return MessageRepository(smsSender) //whatsAppSender
    }
}
