package com.uan.epilepsyalarm20.utils

import android.content.Context
import android.content.Intent
import com.uan.epilepsyalarm20.EmergencyActivity

object EmergencyLauncher {

    fun launch(context: Context) {
        val intent = Intent(context, EmergencyActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }

        context.startActivity(intent)
    }
}