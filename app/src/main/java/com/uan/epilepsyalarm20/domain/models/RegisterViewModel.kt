package com.uan.epilepsyalarm20.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uan.epilepsyalarm20.data.local.entities.UserEntity
import com.uan.epilepsyalarm20.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    //var id by mutableLongStateOf(0L)
    var name by mutableStateOf("")
    var lastName by mutableStateOf("")
    var bloodType by mutableStateOf("")
    var documentType by mutableStateOf("")
    var document by mutableStateOf("")

    suspend fun getUser() = userRepository.getUser()

    fun saveUser() {
        viewModelScope.launch {
            val user = UserEntity(
                id=1,
                nombre=name,
                apellido=lastName,
                tipoDeSangre=bloodType,
                tipoDeDocumento=documentType,
                numeroDeDocumento=document
            )
            userRepository.insertUser(user)
        }
    }

}

enum class BloodType(private val displayName: String){
    SELECTOPTION("Seleccionar tipo de sangre"),
    OPOS("O+"),
    ONEG("O-"),
    APOS("A+"),
    ANEG("A-"),
    BPOS("B+"),
    BNEG("B-"),
    ABPOS("AB+"),
    ABNEG("AB-");

    override fun toString(): String = displayName

    companion object {
        fun toBloodType(displayName: String): BloodType {
            return BloodType.entries.find { it.displayName == displayName } ?: SELECTOPTION
        }
    }
}

enum class DocumentType(private val displayName: String){
    SELECTOPTION("Seleccionar tipo de documento"),
    CEDULACIUDADANIA("Cédula de Ciudadanía"),
    TARJETADEIDENTIDAD("Tarjeta de Identidad"),
    CEDULADEEXTRANJERIA("Cédula de Extranjería");
    override fun toString(): String = displayName

    companion object {
        fun toDocumentType(displayName: String): DocumentType {
            return DocumentType.entries.find { it.displayName == displayName } ?: SELECTOPTION
        }
    }
}