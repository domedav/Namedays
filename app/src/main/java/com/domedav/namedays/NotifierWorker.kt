package com.domedav.namedays

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.app.NotificationManager
import androidx.core.app.NotificationCompat

class NotifierWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        const val CHANNEL_ID = "NamedaysNotifier"
    }

    override fun doWork(): Result {
        println("Fetching data for notifications")
        val data = fetchLocalData()
        println("Sending notification...")
        sendNotification(data, this.applicationContext)
        println("Done")
        return Result.success()
    }

    private fun fetchLocalData(): String {
        println("Obtaining current namedays string...")
        return NamedaysHelper.getStringResource(this.applicationContext)
    }

    private fun sendNotification(data: String, context: Context) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_sent_header))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(data))
            .setSmallIcon(R.drawable.rounded_barefoot)
            .build()

        println("Cancelling old notification and sending new one...")
        notificationManager.cancel(1)
        notificationManager.notify(1, notification)
        println("Done")
    }
}
