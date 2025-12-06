package com.example.tb.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tb.ui.navigation.Screen
import com.example.tb.ui.navigation.screens
import com.example.tb.ui.screens.home.HomeScreen
import com.example.tb.ui.screens.profile.ProfileScreen
import com.example.tb.ui.theme.TBTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                // Получаем текущий маршрут
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Очищаем бэкстэк при нажатии на элемент навигации
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Запускаем один экземпляр экрана
                                launchSingleTop = true
                                // Восстанавливаем состояние
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Определяем граф навигации
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Главный экран
            composable(Screen.Home.route) {
                HomeScreen()
            }

            // Экран профиля
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TBTheme {
        MainScreen()
    }
}