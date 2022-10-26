package com.github.kr328.clash

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.kr328.clash.TaskerSettingsActivity.Companion.from
import com.github.kr328.clash.common.log.Log
import com.github.kr328.clash.design.model.TaskerSettings
import com.github.kr328.clash.service.ProfileProcessor
import com.github.kr328.clash.service.StatusProvider
import com.github.kr328.clash.util.startClashService
import com.github.kr328.clash.util.stopClashService
import kotlinx.coroutines.runBlocking

class TaskerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val settings = TaskerSettings.from(intent)
        Log.i("TaskerReceiver received settings: $settings")

        settings.profile?.let {
            runBlocking {
                ProfileProcessor.active(context, it)
            }
        }

        if (settings.turnOn) {
            if (!StatusProvider.serviceRunning) {
                context.startClashService()
            }
        } else {
            if (settings.profile == null) {
                context.stopClashService()
            }
        }
    }
}