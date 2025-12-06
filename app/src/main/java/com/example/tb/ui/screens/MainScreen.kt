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
import com.example.tb.ui.navigation.bottomScreens

import com.example.tb.ui.screens.home.HomeScreen
import com.example.tb.ui.screens.profile.ProfileScreen
import com.example.tb.ui.screens.settings.SettingsMainScreen
import com.example.tb.ui.screens.system_cold.CoolingRulesScreen
import com.example.tb.ui.theme.TBTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomScreens.forEach { screen ->
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
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Главный экран
            composable(Screen.Home.route) {
                HomeScreen(
                   /* onPurchasesClick = {
                        navController.navigate(Screen.Purchases.route)
                    },
                    onHistoryClick = {
                        navController.navigate(Screen.History.route)
                    },
                    onAddPurchaseClick = {
                        navController.navigate(Screen.AddPurchase.route)
                    },*/
                    onSettingsClick = {
                        navController.navigate(Screen.Settings.route)
                    },
                    onCoolingRulesClick = {
                        navController.navigate(Screen.CoolingRules.route)
                    }
                )
            }

            /* Экран покупок
            composable(Screen.Purchases.route) {
                PurchasesScreen(
                    onBackClick = { navController.navigateUp() },
                    onAddPurchaseClick = {
                        navController.navigate(Screen.AddPurchase.route)
                    }
                )
            }

            // Экран истории
            composable(Screen.History.route) {
                HistoryScreen(
                    onBackClick = { navController.navigateUp() }
                )
            }

            // Экран профиля
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onHistoryClick = {
                        navController.navigate(Screen.History.route)
                    }
                )
            }*/

            // Экран настроек
            composable(Screen.Settings.route) {
                SettingsMainScreen(
                    onBackClick = { navController.navigateUp() },
                    onCoolingRulesClick = {
                        navController.navigate(Screen.CoolingRules.route)
                    }
                )
            }

            /* Экран добавления покупки
            composable(Screen.AddPurchase.route) {
                AddPurchaseScreen(
                    onBackClick = { navController.navigateUp() },
                    onPurchaseAdded = {
                        navController.popBackStack()
                        navController.navigate(Screen.Purchases.route)
                    }
                )
            }
*/
            // Экран правил охлаждения
            composable(Screen.CoolingRules.route) {
                CoolingRulesScreen(
                    onBackClick = { navController.navigateUp() }
                )
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