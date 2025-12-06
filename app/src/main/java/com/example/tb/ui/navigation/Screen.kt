package com.example.tb.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen("home", "Главная", Icons.Default.Home)
    object Profile : Screen("profile", "Профиль", Icons.Default.Person)
    object Settings : Screen("settings", "Настройки", Icons.Default.Settings)

    // Дополнительные экраны
    object CoolingRules : Screen("cooling_rules", "Охлаждение", Icons.Default.Settings)
    object SettingsDetails : Screen("settings_details", "Настройки", Icons.Default.Settings)
}

val bottomScreens = listOf(
    Screen.Home,
    Screen.Profile,
    Screen.Settings
)

val allScreens = listOf(
    Screen.Home,
    Screen.Profile,
    Screen.Settings,
    Screen.CoolingRules,
    Screen.SettingsDetails
)