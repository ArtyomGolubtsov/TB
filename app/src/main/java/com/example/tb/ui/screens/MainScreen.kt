package com.example.tb.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.tb.ui.navigation.Screen
import com.example.tb.ui.screens.home.HomeScreen
import com.example.tb.ui.screens.setting.SettingsMainScreen
import com.example.tb.ui.screens.cooling.CoolingRulesScreen
import com.example.tb.ui.screens.buyers.PurchaseScreen
import com.example.tb.ui.screens.addbuyers.AddPurchaseScreen
import com.example.tb.ui.screens.finance.FinanceScreen
import com.example.tb.ui.screens.blacklist.BlackListScreen
import com.example.tb.ui.screens.blacklist.AddCategoryScreen
import com.example.tb.ui.screens.buyers.PurchaseViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Общий ViewModel для списка и экрана добавления
    val purchaseViewModel: PurchaseViewModel = viewModel()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(0.dp)
        ) {

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

            composable(Screen.Blacklist.route) {
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
                        navController.navigateUp()
                    }
                )
            }

            composable(Screen.Finance.route) {
                FinanceScreen(
                    onBackClick = { navController.navigateUp() }
                )
            }

            // Экран списка покупок
            composable(Screen.Purchases.route) {
                PurchaseScreen(
                    viewModel = purchaseViewModel,
                    onBackClick = { navController.navigateUp() },
                    onAddPurchaseClick = {
                        navController.navigate(Screen.AddPurchase.route)
                    }
                )
            }

            // Экран добавления покупки
            composable(Screen.AddPurchase.route) {
                AddPurchaseScreen(
                    viewModel = purchaseViewModel,
                    onBackClick = { navController.navigateUp() }
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
