package com.uan.epilepsyalarm20.data.service

import android.accessibilityservice.AccessibilityService
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.uan.epilepsyalarm20.data.repository.PreferencesManager
import com.uan.epilepsyalarm20.utils.EmergencyLauncher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class VolumeButtonAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    //private var volumePressTimestamps = ArrayDeque<Long>()

    private var emergencyJob: Job? = null
    private var coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var _emergencyMethod: String = "long_press"

    private var _timeLimit: Long = 5000L

    override fun onServiceConnected() {
        super.onServiceConnected()
        // Obtiene la configuración actual de emergencia del usuario
        coroutineScope.launch {
            preferencesManager.emergencyMethodFlow.collect { newMethod ->
                _emergencyMethod = newMethod
            }
        }
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                when (event.action) {
                    KeyEvent.ACTION_DOWN -> {
                        when (_emergencyMethod) {
//                            "two_touch" -> {
//                                handleVolumeClick(2L)
//                            }
//                            "three_touch" -> {
//                                handleVolumeClick(3L)
//                            }
                            "long_press" -> {
                                emergencyJob = coroutineScope.launch {
                                    delay(_timeLimit.milliseconds)
                                    EmergencyLauncher.launch(this@VolumeButtonAccessibilityService)
                                }
                            }
                        }
                    }
                    KeyEvent.ACTION_UP -> {
                        when(_emergencyMethod) {
                            "long_press" -> {
                                emergencyJob?.cancel()
                                emergencyJob = null
                            }
                        }
                    }
                }
            }
        }
        return super.onKeyEvent(event)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    // Para manejar 2 o 3 veces el botón de volumen
//    private fun handleVolumeClick(times: Long) {
//        val now = System.currentTimeMillis()
//
//        volumePressTimestamps.addLast(now)
//
//        // Vacía los timestamps que han pasado más de 5 segundos
//        while(volumePressTimestamps.isNotEmpty() && now - volumePressTimestamps.first() > _timeLimit) {
//            volumePressTimestamps.removeFirst()
//        }
//
//        if(volumePressTimestamps.size.toLong() == times) {
//            launchEmergencyActivity()
//            volumePressTimestamps.clear()
//        }
//    }
}
