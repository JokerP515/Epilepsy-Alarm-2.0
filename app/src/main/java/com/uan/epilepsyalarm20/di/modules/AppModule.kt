package com.uan.epilepsyalarm20.di.modules

import android.app.Application
import androidx.room.Room
import com.uan.epilepsyalarm20.data.local.AppDatabase
import com.uan.epilepsyalarm20.data.local.dao.EmergencyContactDao
import com.uan.epilepsyalarm20.data.local.dao.UserDao
import com.uan.epilepsyalarm20.data.repository.EmergencyContactRepository
import com.uan.epilepsyalarm20.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository = UserRepository(userDao)

    @Provides
    @Singleton
    fun provideEmergencyContactRepository(emergencyContactDao: EmergencyContactDao): EmergencyContactRepository =
        EmergencyContactRepository(emergencyContactDao)
}