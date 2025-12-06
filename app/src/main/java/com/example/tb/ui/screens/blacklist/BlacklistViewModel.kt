package com.example.tb.ui.screens.blacklist

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.tb.R // Добавлен импорт для доступа к ресурсам

// Модели данных
data class BlacklistCategory(
    val id: Int,
    val name: String,
    val icon: String, // Оставляем строковый идентификатор
    val occurrence: Int,
    val totalSpent: Double,
    val isSelected: Boolean = false,
    val isBlocked: Boolean = false,
    val daysBlocked: Int = 0
)

data class MonthlyStat(
    val month: String,
    val spent: Double,
    val average: Double
)

data class BlacklistState(
    val categories: List<BlacklistCategory> = emptyList(),
    val monthlyStats: List<MonthlyStat> = emptyList(),
    val selectedCategory: BlacklistCategory? = null
)

class BlacklistViewModel : ViewModel() {
    private val _state = MutableStateFlow(BlacklistState())
    val state: StateFlow<BlacklistState> = _state.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val categories = listOf(
                BlacklistCategory(
                    id = 1,
                    name = "Одежда",
                    icon = "clothes",
                    occurrence = 7,
                    totalSpent = 22000.0,
                    isSelected = true
                ),
                BlacklistCategory(
                    id = 2,
                    name = "Техника",
                    icon = "tech",
                    occurrence = 5,
                    totalSpent = 45000.0
                ),
                BlacklistCategory(
                    id = 3,
                    name = "Игры",
                    icon = "games",
                    occurrence = 3,
                    totalSpent = 12000.0
                ),
                BlacklistCategory(
                    id = 4,
                    name = "Косметика",
                    icon = "cosmetics",
                    occurrence = 4,
                    totalSpent = 8000.0
                ),
                BlacklistCategory(
                    id = 5,
                    name = "Ювелирные изделия",
                    icon = "jewelry",
                    occurrence = 2,
                    totalSpent = 35000.0
                )
            )

            val monthlyStats = listOf(
                MonthlyStat("Ноябрь", 12000.0, 4000.0),
                MonthlyStat("Декабрь", 15000.0, 5000.0),
                MonthlyStat("Январь", 10000.0, 3333.0),
                MonthlyStat("Февраль", 18000.0, 6000.0)
            )

            _state.update {
                it.copy(
                    categories = categories,
                    monthlyStats = monthlyStats,
                    selectedCategory = categories.first()
                )
            }
        }
    }

    fun selectCategory(category: BlacklistCategory) {
        _state.update { currentState ->
            val updatedCategories = currentState.categories.map {
                it.copy(isSelected = it.id == category.id)
            }
            currentState.copy(
                categories = updatedCategories,
                selectedCategory = category
            )
        }
    }

    fun blockCategory(categoryId: Int, days: Int) {
        _state.update { currentState ->
            val updatedCategories = currentState.categories.map {
                if (it.id == categoryId) {
                    it.copy(isBlocked = true, daysBlocked = days)
                } else {
                    it
                }
            }
            currentState.copy(categories = updatedCategories)
        }
    }

    fun unblockCategory(categoryId: Int) {
        _state.update { currentState ->
            val updatedCategories = currentState.categories.map {
                if (it.id == categoryId) {
                    it.copy(isBlocked = false, daysBlocked = 0)
                } else {
                    it
                }
            }
            currentState.copy(categories = updatedCategories)
        }
    }
}

// Вспомогательная функция для форматирования денег
fun formatMoney(amount: Double): String {
    return "%,d".format(amount.toInt()).replace(',', ' ')
}