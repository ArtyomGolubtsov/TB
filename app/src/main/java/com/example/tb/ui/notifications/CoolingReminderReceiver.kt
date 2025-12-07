package com.example.tb.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.tb.ui.MainActivity
import com.example.tb.R

class CoolingReminderReceiver : BroadcastReceiver() {

    companion object {
        private const val CHANNEL_ID = "cooling_reminder_channel"
        private const val NOTIFICATION_ID = 2001
    }

    override fun onReceive(context: Context, intent: Intent?) {
        showNotification(context)
    }

    private fun showNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Канал для Android 8+
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Охлаждение покупок",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Напоминания о том, что скоро заканчивается период охлаждения"
            enableLights(true)
            lightColor = Color.YELLOW
        }
        notificationManager.createNotificationChannel(channel)

        // Интент для открытия экрана «Покупки»
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("open_screen", "purchases")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // добавь такой ресурс
            .setContentTitle("Скоро заканчивается охлаждение")
            .setContentText("Проверь свои покупки и реши, что делать дальше.")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}