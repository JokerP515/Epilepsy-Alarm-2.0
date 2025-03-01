package com.uan.epilepsyalarm20.data.repository

import com.uan.epilepsyalarm20.data.local.dao.UserDao
import com.uan.epilepsyalarm20.data.local.entities.UserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun getUserById(id: Long) = userDao.getUserById(id)

}