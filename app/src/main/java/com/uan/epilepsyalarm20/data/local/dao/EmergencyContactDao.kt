package com.uan.epilepsyalarm20.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity

@Dao
interface EmergencyContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyContact(emergencyContact: EmergencyContactEntity)

    @Query("SELECT * FROM emergency_contacts WHERE userId = :userId")
    suspend fun getEmergencyContactsByUserId(userId: Long): List<EmergencyContactEntity> // Posiblemente toque cambiarlo a un Flow

    @Query("DELETE FROM emergency_contacts WHERE id = :id")
    suspend fun deleteEmergencyContactById(id: Long)

}