package com.example.tb.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.tb.ui.notifications.NotificationScheduler
import com.example.tb.ui.navigation.Screen
import com.example.tb.ui.screens.MainScreen
import com.example.tb.ui.theme.TBTheme

class MainActivity : ComponentActivity() {

    // Новый API для запроса разрешений
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Разрешение получено — вызываем тестовое уведомление
                //NotificationScheduler.scheduleDebugNotification(this) //тест уведов
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermission()

        // Обработка перехода по уведомлению
        val openScreen = intent?.getStringExtra("open_screen")
        val startRoute = if (openScreen == "purchases") Screen.Purchases.route else Screen.Home.route

        setContent {
            TBTheme {
                MainScreen(startDestinationRoute = startRoute)
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                // Используем новый ActivityResult API
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                NotificationScheduler.scheduleDebugNotification(this)
            }
        } else {
            NotificationScheduler.scheduleDebugNotification(this)
        }
    }
}
