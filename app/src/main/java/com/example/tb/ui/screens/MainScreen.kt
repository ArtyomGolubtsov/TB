package com.example.tb.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tb.ui.navigation.Screen
import com.example.tb.ui.screens.addbuyers.AddPurchaseScreen
import com.example.tb.ui.screens.blacklist.AddCategoryScreen
import com.example.tb.ui.screens.blacklist.BlackListScreen
import com.example.tb.ui.screens.blacklist.BlacklistViewModel
import com.example.tb.ui.screens.buyers.PurchaseScreen
import com.example.tb.ui.screens.buyers.PurchaseViewModel
import com.example.tb.ui.screens.cooling.CoolingRulesScreen
import com.example.tb.ui.screens.cooling.CoolingRulesViewModel
import com.example.tb.ui.screens.finance.FinanceScreen
import com.example.tb.ui.screens.home.HomeScreen
import com.example.tb.ui.screens.setting.SettingsMainScreen
import com.example.tb.ui.screens.setting.SettingsViewModel

@Composable
fun MainScreen(startDestinationRoute: String = Screen.Home.route) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Получаем все ViewModel
    val purchaseViewModel: PurchaseViewModel = viewModel()
    val coolingRulesViewModel: CoolingRulesViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val blacklistViewModel: BlacklistViewModel = viewModel() // Теперь эта строка работает

    // Загружаем данные при инициализации
    LaunchedEffect(Unit) {
        coolingRulesViewModel.loadRules(context)
        settingsViewModel.loadSettings(context)
        // Загрузка черного списка не требуется, так как он уже загружается в init
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestinationRoute,
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
                    onBlacklistClick = {
                        navController.navigate(Screen.Blacklist.route)
                    },
                    onPurchasesClick = {
                        navController.navigate(Screen.Purchases.route)
                    }
                )
            }

            // Черный список
            composable(Screen.Blacklist.route) {
                BlackListScreen(
                    viewModel = blacklistViewModel, // Передаем ViewModel
                    onBackClick = { navController.navigateUp() },
                    onAddCategoryClick = {
                        navController.navigate(Screen.AddCategory.route)
                    }
                )
            }


            // Финансы
            composable(Screen.Finance.route) {
                FinanceScreen(
                    onBackClick = { navController.navigateUp() }
                )
            }

            // Список покупок
            composable(Screen.Purchases.route) {
                PurchaseScreen(
                    viewModel = purchaseViewModel,
                    onBackClick = { navController.navigateUp() },
                    onAddPurchaseClick = {
                        navController.navigate(Screen.AddPurchase.route)
                    }
                )
            }

            composable(Screen.AddCategory.route) {
                AddCategoryScreen(
                    onBackClick = { navController.navigateUp() },
                    onSaveClick = { selectedCategories ->
                        navController.navigateUp()
                    }
                )
            }

            // Добавление покупки (с расчетом охлаждения)
            composable(Screen.AddPurchase.route) {
                AddPurchaseScreen(
                    purchaseViewModel = purchaseViewModel,
                    coolingRulesViewModel = coolingRulesViewModel,
                    settingsViewModel = settingsViewModel,
                    blacklistViewModel = blacklistViewModel, // Теперь работает
                    onBackClick = { navController.navigateUp() },
                    onAddClick = {
                        navController.navigateUp()
                    }
                )
            }

            // Настройки
            composable(Screen.Settings.route) {
                SettingsMainScreen(
                    settingsViewModel = settingsViewModel,
                    onBackClick = { navController.navigateUp() },
                    onCoolingRulesClick = {
                        navController.navigate(Screen.CoolingRules.route)
                    },
                    onSaveSuccess = {
                        // Можно добавить toast или сообщение об успехе
                    }
                )
            }

            // Правила охлаждения
            composable(Screen.CoolingRules.route) {
                CoolingRulesScreen(
                    coolingRulesViewModel = coolingRulesViewModel,
                    onBackClick = { navController.navigateUp() }
                )
            }
        }
    }
}