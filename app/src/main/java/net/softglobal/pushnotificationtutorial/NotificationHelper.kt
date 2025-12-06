package net.softglobal.pushnotificationtutorial

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "tb_notification_channel"
        const val CHANNEL_NAME = "T-Bank Уведомления"
        const val CHANNEL_GOALS = "tb_goals_channel"
        const val CHANNEL_URGENT = "tb_urgent_channel"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Основной канал
            val mainChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Канал для важных уведомлений T-Bank"
                enableLights(true)
                enableVibration(true)
            }

            // Канал для целей
            val goalsChannel = NotificationChannel(
                CHANNEL_GOALS,
                "Цели и накопления",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления о прогрессе финансовых целей"
                enableLights(true)
                lightColor = 0xFF4CAF50.toInt()
            }

            // Канал для срочных уведомлений
            val urgentChannel = NotificationChannel(
                CHANNEL_URGENT,
                "Срочные уведомления",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Важные предупреждения"
                enableLights(true)
                lightColor = 0xFFFF0000.toInt()
                enableVibration(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

            notificationManager.createNotificationChannels(
                listOf(mainChannel, goalsChannel, urgentChannel)
            )
        }
    }

    fun showNotification(title: String, message: String, notificationId: Int = 1) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    fun showGoalNotification(
        title: String,
        message: String,
        notificationId: Int,
        isUrgent: Boolean = false
    ) {
        val channelId = if (isUrgent) CHANNEL_URGENT else CHANNEL_GOALS

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(
                if (isUrgent) NotificationCompat.PRIORITY_HIGH
                else NotificationCompat.PRIORITY_DEFAULT
            )
            .setAutoCancel(true)
            .setColor(0xFF4CAF50.toInt())
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }
}