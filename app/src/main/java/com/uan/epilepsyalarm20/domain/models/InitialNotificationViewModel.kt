package com.uan.epilepsyalarm20.domain.models

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.uan.epilepsyalarm20.data.repository.PreferencesManager

@HiltViewModel
class InitialNotificationViewModel @Inject constructor(
    private val preferences: PreferencesManager
) : ViewModel() {
    fun notShowAgain() {
        preferences.setPopupShown(false)
    }
}