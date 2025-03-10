package com.uan.epilepsyalarm20.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uan.epilepsyalarm20.data.local.entities.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Query("UPDATE users SET mensajeEmergencia = :message WHERE id = 1")
    suspend fun updateEmergencyMessage(message: String)

}