package com.example.tb.ui.screens.cooling

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CoolingRulesState(
    val dayLimit: String = "",
    val weekMinLimit: String = "",
    val weekMaxLimit: String = "",
    val monthLimit: String = ""
)

class CoolingRulesViewModel : ViewModel() {

    private val _state = MutableStateFlow(CoolingRulesState())
    val state: StateFlow<CoolingRulesState> = _state.asStateFlow()

    fun updateDayLimit(value: String) {
        _state.update { it.copy(dayLimit = value) }
    }

    fun updateWeekMinLimit(value: String) {
        _state.update { it.copy(weekMinLimit = value) }
    }

    fun updateWeekMaxLimit(value: String) {
        _state.update { it.copy(weekMaxLimit = value) }
    }

    fun updateMonthLimit(value: String) {
        _state.update { it.copy(monthLimit = value) }
    }

    fun loadRules(context: Context) {
        val prefs = context.getSharedPreferences("cooling_rules", Context.MODE_PRIVATE)

        val day = prefs.getInt("day_limit", -1)
        val weekMin = prefs.getInt("week_min_limit", -1)
        val weekMax = prefs.getInt("week_max_limit", -1)
        val month = prefs.getInt("month_limit", -1)

        _state.update { old ->
            old.copy(
                dayLimit = if (day >= 0) day.toString() else "",
                weekMinLimit = if (weekMin >= 0) weekMin.toString() else "",
                weekMaxLimit = if (weekMax >= 0) weekMax.toString() else "",
                monthLimit = if (month >= 0) month.toString() else ""
            )
        }
    }

    fun saveRules(
        context: Context,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val current = state.value
            val errors = mutableListOf<String>()

            // Простейшая валидация — не обязательно, но полезно
            fun String.toIntOrZeroOrError(fieldName: String): Int? {
                if (isEmpty()) return 0
                return this.toIntOrNull() ?: run {
                    errors.add("Некорректное значение для поля \"$fieldName\". Используйте только цифры.")
                    null
                }
            }

            val day = current.dayLimit.toIntOrZeroOrError("Лимит на 1 день")
            val weekMin = current.weekMinLimit.toIntOrZeroOrError("Минимум за 1 неделю")
            val weekMax = current.weekMaxLimit.toIntOrZeroOrError("Максимум за 1 неделю")
            val month = current.monthLimit.toIntOrZeroOrError("Лимит за 1 месяц")

            if (errors.isNotEmpty() || day == null || weekMin == null || weekMax == null || month == null) {
                onError(errors.joinToString("\n"))
                return@launch
            }

            val prefs = context.getSharedPreferences("cooling_rules", Context.MODE_PRIVATE)
            with(prefs.edit()) {
                putInt("day_limit", day)
                putInt("week_min_limit", weekMin)
                putInt("week_max_limit", weekMax)
                putInt("month_limit", month)
                apply()
            }

            onSuccess()
        }
    }
}
