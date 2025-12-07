package com.example.tb.ui.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.tb.ui.screens.setting.NotificationFrequency

object NotificationScheduler {

    private const val COOLING_REMINDER_REQUEST_CODE = 1001

    fun scheduleCoolingReminder(
        context: Context,
        frequency: NotificationFrequency
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // На всякий случай отменим старый будильник
        cancelCoolingReminder(context)

        val interval = frequency.intervalMillis

        val intent = Intent(context, CoolingReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            COOLING_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val firstTriggerAt = System.currentTimeMillis() + interval

        // Повторяющийся будильник (НЕ точный, но безопасный)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            firstTriggerAt,
            interval,
            pendingIntent
        )
    }

    fun cancelCoolingReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, CoolingReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            COOLING_REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Плановое уведомление по частоте (можно будет донастроить под реальные правила)
     */
    fun scheduleCoolingNotification(context: Context, frequency: NotificationFrequency) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            100,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAt = System.currentTimeMillis() + frequency.intervalMillis

        try {
            // Используем обычный set — без точного будильника, зато без падений
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    /**
     * 🔹 ОТЛАДОЧНОЕ УВЕДОМЛЕНИЕ — через 5 секунд после запуска приложения
     */
    fun scheduleDebugNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            999, // другой requestCode, чтобы не пересекаться
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAt = System.currentTimeMillis() + 5_000L // через 5 секунд

        try {
            // Тоже используем обычный set, чтобы не требовался SCHEDULE_EXACT_ALARM
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
