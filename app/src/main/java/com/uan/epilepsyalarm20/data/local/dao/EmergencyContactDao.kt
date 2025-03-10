package com.uan.epilepsyalarm20.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyContact(emergencyContact: EmergencyContactEntity)

    @Query("SELECT * FROM emergency_contacts")
    fun getEmergencyContacts(): Flow<List<EmergencyContactEntity>>

    @Query("DELETE FROM emergency_contacts WHERE id = :id")
    suspend fun deleteEmergencyContactById(id: Long)

}