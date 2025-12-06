package com.example.tb.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// Определение экранов для навигации
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen("home", "Главная", Icons.Default.Home)
    object Profile : Screen("profile", "Профиль", Icons.Default.Person)
    object Settings : Screen("settings", "Настройки", Icons.Default.Settings)
}

// Список всех экранов для BottomNavigation
val screens = listOf(
    Screen.Home,
    Screen.Profile,
    Screen.Settings
)