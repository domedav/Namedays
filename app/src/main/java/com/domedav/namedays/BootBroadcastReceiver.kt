package com.domedav.namedays

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            println("Action Boot Completed!\nSetting up worker!")
            WorkerScheduler.scheduleDailyWork(context)
        }
    }
}
