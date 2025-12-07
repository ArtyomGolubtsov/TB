package com.example.tb.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tb.R
import com.example.tb.ui.MainActivity

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        // 1. СОЗДАЁМ КАНАЛ (обязательно!)
        val channelId = "cooling_channel"
        val channelName = "Охлаждение"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(channelId, channelName, importance)

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)

        // 2. PendingIntent -> открыть покупки
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("open_screen", "purchases")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 3. Само уведомление
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Охлаждение почти закончилось")
            .setContentText("Скоро заканчивается период охлаждения. Проверь свои покупки.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(5001, notification)
    }
}
