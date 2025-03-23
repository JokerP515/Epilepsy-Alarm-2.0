package com.uan.epilepsyalarm20.data.repository

import com.uan.epilepsyalarm20.data.local.dao.UserDao
import com.uan.epilepsyalarm20.data.local.entities.UserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun userExists() = userDao.userExists()

    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun getUser() = userDao.getUser()

    suspend fun updateEmergencyMessage(message: String) = userDao.updateEmergencyMessage(message)

    suspend fun updateEmergencyInstructions(instructions: String) = userDao.updateEmergencyInstructions(instructions)

    suspend fun getEmergencyMessage() = userDao.getEmergencyMessage()

    suspend fun getEmergencyInstructions() = userDao.getEmergencyInstructions()

}