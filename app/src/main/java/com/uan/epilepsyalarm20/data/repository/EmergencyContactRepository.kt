package com.uan.epilepsyalarm20.data.repository

import com.uan.epilepsyalarm20.data.local.dao.EmergencyContactDao
import com.uan.epilepsyalarm20.data.local.entities.EmergencyContactEntity
import javax.inject.Inject

class EmergencyContactRepository @Inject constructor(
    private val emergencyContactDao: EmergencyContactDao
) {
    suspend fun insertEmergencyContact(emergencyContact: EmergencyContactEntity) =
        emergencyContactDao.insertEmergencyContact(emergencyContact)

    suspend fun deleteEmergencyContactById(id: Long) = 
        emergencyContactDao.deleteEmergencyContactById(id)

    suspend fun getEmergencyContactsByUserId(userId: Long): List<EmergencyContactEntity> =
        emergencyContactDao.getEmergencyContactsByUserId(userId)

}