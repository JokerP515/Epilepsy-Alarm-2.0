package com.uan.epilepsyalarm20.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uan.epilepsyalarm20.data.local.dao.EmergencyContactDao
import com.uan.epilepsyalarm20.data.local.dao.UserDao
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import com.uan.epilepsyalarm20.data.local.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        EmergencyContactEntity::class
    ],
    version = 2
) abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val emergencyContactDao: EmergencyContactDao
}