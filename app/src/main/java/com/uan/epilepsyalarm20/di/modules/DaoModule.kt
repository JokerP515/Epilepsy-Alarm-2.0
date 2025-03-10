package com.uan.epilepsyalarm20.di.modules

import com.uan.epilepsyalarm20.data.local.AppDatabase
import com.uan.epilepsyalarm20.data.local.dao.EmergencyContactDao
import com.uan.epilepsyalarm20.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao

    @Provides
    @Singleton
    fun provideEmergencyContactDao(db: AppDatabase): EmergencyContactDao = db.emergencyContactDao

}