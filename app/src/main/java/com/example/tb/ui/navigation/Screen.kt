package com.example.tb.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.List // Для черного списка
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
    object Purchases : Screen("purchases", "Покупки", Icons.Default.ShoppingCart)
    object AddPurchase : Screen("add_purchase", "Добавить покупку", Icons.Default.ShoppingCart)
    object Finance : Screen("finance", "Финансы", Icons.Default.ShoppingCart)

    // Экраны черного списка
    object Blacklist : Screen("blacklist", "Черный список", Icons.Default.List)
    object AddCategory : Screen("add_category", "Добавить категорию", Icons.Default.List)
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
    Screen.SettingsDetails,
    Screen.Purchases,
    Screen.AddPurchase,
    Screen.Finance,
    Screen.Blacklist,      // ← ДОБАВЬТЕ
    Screen.AddCategory     // ← ДОБАВЬТЕ
)