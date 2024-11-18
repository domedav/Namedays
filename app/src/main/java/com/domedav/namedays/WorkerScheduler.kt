package com.domedav.namedays

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class WorkerScheduler{
    companion object{
        fun scheduleDailyWork(context: Context){
            val currentTime = Calendar.getInstance()
            val targetTime = Calendar.getInstance()

            targetTime.set(Calendar.HOUR_OF_DAY, 9)
            targetTime.set(Calendar.MINUTE, 0)
            targetTime.set(Calendar.SECOND, 0)

            println("Setting up target time for worker...")

            if (currentTime.after(targetTime)) {
                targetTime.add(Calendar.DATE, 1)
                println("1 day delay needed!")
            }

            val delayMillis = targetTime.timeInMillis - currentTime.timeInMillis
            println("Worker will work in $delayMillis ms")

            val periodicWorkRequest = PeriodicWorkRequestBuilder<NotifierWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).cancelAllWork()
            println("Cancelled all owned active workers")
            WorkManager.getInstance(context).enqueue(periodicWorkRequest)
            println("Set up our worker!")
        }
    }
}