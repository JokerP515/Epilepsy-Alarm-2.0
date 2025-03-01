package com.uan.epilepsyalarm20.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val apellido: String,
    val tipoDeSangre: String,
    val tipoDeDocumento: String,
    val numeroDeDocumento: String
)
