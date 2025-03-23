package com.uan.epilepsyalarm20.ui.events

sealed class UserEvent {
    data class OnIdChange(val id: Long) : UserEvent()
    data class OnNameChange(val name: String) : UserEvent()
    data class OnLastNameChange(val lastName: String) : UserEvent()
    data class OnBloodTypeChange(val bloodType: String) : UserEvent()
    data class OnDocumentTypeChange(val documentType: String) : UserEvent()
    data class OnDocumentChange(val document: String) : UserEvent()
    data object onSave : UserEvent()
}