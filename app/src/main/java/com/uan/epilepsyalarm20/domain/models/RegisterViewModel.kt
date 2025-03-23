package com.uan.epilepsyalarm20.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uan.epilepsyalarm20.data.local.entities.UserEntity
import com.uan.epilepsyalarm20.data.repository.UserRepository
import com.uan.epilepsyalarm20.ui.events.UserEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var id by mutableLongStateOf(0L)
    var name by mutableStateOf("")
    var lastName by mutableStateOf("")
    var bloodType by mutableStateOf("")
    var documentType by mutableStateOf("")
    var document by mutableStateOf("")

    fun saveUser() {
        viewModelScope.launch {
            val user = UserEntity(
                id=id,
                nombre=name,
                apellido=lastName,
                tipoDeSangre=bloodType,
                tipoDeDocumento=documentType,
                numeroDeDocumento=document
            )
            userRepository.insertUser(user)
        }
    }

    fun onEvent(event: UserEvent){
        when(event) {
            is UserEvent.OnNameChange -> name = event.name
            is UserEvent.OnIdChange -> id = event.id
            is UserEvent.OnLastNameChange -> lastName = event.lastName
            is UserEvent.OnBloodTypeChange -> bloodType = event.bloodType
            is UserEvent.OnDocumentTypeChange -> documentType = event.documentType
            is UserEvent.OnDocumentChange -> document = event.document
            is UserEvent.onSave -> {
                saveUser()
                reset()
            }
        }
    }

    private fun reset() {
        name = ""
        id = 0L
        lastName = ""
        bloodType = ""
        documentType = ""
        document = ""
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
}

enum class DocumentType(private val displayName: String){
    SELECTOPTION("Seleccionar tipo de documento"),
    CEDULACIUDADANIA("Cédula de Ciudadanía"),
    TARJETADEIDENTIDAD("Tarjeta de Identidad"),
    CEDULADEEXTRANJERIA("Cédula de Extranjería");
    override fun toString(): String = displayName
}