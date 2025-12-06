package com.example.tb.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tb.ui.navigation.Screen
import com.example.tb.ui.screens.home.HomeScreen
import com.example.tb.ui.screens.setting.SettingsMainScreen
import com.example.tb.ui.screens.cooling.CoolingRulesScreen
import com.example.tb.ui.screens.buyers.PurchaseScreen
import com.example.tb.ui.screens.addbuyers.AddPurchaseScreen
import com.example.tb.ui.screens.finance.FinanceScreen
import com.example.tb.ui.screens.blacklist.BlackListScreen  // ← ИМПОРТ
import com.example.tb.ui.screens.blacklist.AddCategoryScreen // ← ИМПОРТ
import com.example.tb.ui.screens.blacklist.BlacklistViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(0.dp)
        ) {
            // Главный экран
            composable(Screen.Home.route) {
                HomeScreen(
                    onSettingsClick = {
                        navController.navigate(Screen.Settings.route)
                    },
                    onCoolingRulesClick = {
                        navController.navigate(Screen.CoolingRules.route)
                    },
                    onBlacklistClick = {  // ← ОБНОВИТЕ ЭТУ СТРОКУ
                        navController.navigate(Screen.Blacklist.route)
                    },
                    onPurchasesClick = {
                        navController.navigate(Screen.Purchases.route)
                    }
                )
            }

            // Экран черного списка
            composable(Screen.Blacklist.route) {
                BlackListScreen(
                    onBackClick = { navController.navigateUp() },
                    onAddCategoryClick = {
                        navController.navigate(Screen.AddCategory.route)
                    }
                )
            }

            // Экран добавления категории
            composable(Screen.AddCategory.route) {
                AddCategoryScreen(
                    onBackClick = { navController.navigateUp() },
                    onSaveClick = { selectedCategories ->
                        // Обработка сохранения выбранных категорий
                        // Можно передать в ViewModel или сохранить локально
                        navController.navigateUp()
                    }
                )
            }

            composable(Screen.Blacklist.route) {
                val viewModel = remember { BlacklistViewModel() }

                BlackListScreen(
                    onBackClick = { navController.navigateUp() },
                    onAddCategoryClick = {
                        navController.navigate(Screen.AddCategory.route)
                    }
                )
            }

            composable(Screen.AddCategory.route) {
                AddCategoryScreen(
                    onBackClick = { navController.navigateUp() },
                    onSaveClick = { selectedCategories ->
                        // Сохранить выбранные категории
                        // Можно обновить SharedPreferences или локальную БД
                        navController.navigateUp()
                    }
                )
            }

            // Остальные экраны...
            composable(Screen.Finance.route) {
                FinanceScreen(
                    onBackClick = { navController.navigateUp() }
                )
            }

            composable(Screen.Purchases.route) {
                PurchaseScreen(
                    onBackClick = { navController.navigateUp() },
                    onAddPurchaseClick = {
                        navController.navigate(Screen.AddPurchase.route)
                    }
                )
            }

            composable(Screen.AddPurchase.route) {
                AddPurchaseScreen(
                    onBackClick = { navController.navigateUp() },
                    onAddClick = {
                        navController.navigateUp()
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsMainScreen(
                    onBackClick = { navController.navigateUp() },
                    onCoolingRulesClick = {
                        navController.navigate(Screen.CoolingRules.route)
                    }
                )
            }

            composable(Screen.CoolingRules.route) {
                CoolingRulesScreen(
                    onBackClick = { navController.navigateUp() }
                )
            }
        }
    }
}