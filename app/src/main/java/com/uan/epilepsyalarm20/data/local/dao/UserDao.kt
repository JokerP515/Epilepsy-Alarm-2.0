package com.uan.epilepsyalarm20.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uan.epilepsyalarm20.data.local.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT EXISTS (SELECT 1 FROM users LIMIT 1)")
    suspend fun userExists(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Query("UPDATE users SET mensajeEmergencia = :message WHERE id = 1")
    suspend fun updateEmergencyMessage(message: String)

    @Query("UPDATE users SET instruccionesEmergencia = :instructions WHERE id = 1")
    suspend fun updateEmergencyInstructions(instructions: String)

    @Query("SELECT mensajeEmergencia FROM users LIMIT 1")
    suspend fun getEmergencyMessage(): String? = getUser()?.mensajeEmergencia

    @Query("SELECT instruccionesEmergencia FROM users LIMIT 1")
    suspend fun getEmergencyInstructions(): String? = getUser()?.instruccionesEmergencia

}