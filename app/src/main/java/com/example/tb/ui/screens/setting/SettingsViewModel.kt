package com.example.tb.ui.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class SettingsState(
    val notificationFrequency: NotificationFrequency = NotificationFrequency.DAILY,
    val notificationChannel: NotificationChannel = NotificationChannel.PUSH,
    val monthlySavings: String = "",
    val income: String = "",
    val considerSavings: Boolean = true,
    val currentSavings: String = ""
) {
    fun isValid(): Boolean {
        // Проверяем только что поля не пустые
        val monthlySavingsValid = monthlySavings.isNotBlank()
        val incomeValid = income.isNotBlank()
        val currentSavingsValid = !considerSavings || currentSavings.isNotBlank()

        return monthlySavingsValid && incomeValid && currentSavingsValid
    }

    fun getMonthlySavingsAsInt(): Int? {
        return monthlySavings.toIntOrNull()
    }

    fun getIncomeAsInt(): Int? {
        return income.toIntOrNull()
    }

    fun getCurrentSavingsAsInt(): Int? {
        return currentSavings.toIntOrNull()
    }
}

enum class NotificationFrequency(val displayName: String) {
    DAILY("Ежедневно"),
    WEEKLY("Раз в неделю"),
    BIWEEKLY("Раз в две недели"),
    MONTHLY("Раз в месяц")
}

enum class NotificationChannel(val displayName: String) {
    PUSH("Push-уведомления"),
    EMAIL("Email"),
    SMS("SMS"),
    TELEGRAM("Telegram")
}

class SettingsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    fun updateNotificationFrequency(frequency: NotificationFrequency) {
        _state.update { it.copy(notificationFrequency = frequency) }
    }

    fun updateNotificationChannel(channel: NotificationChannel) {
        _state.update { it.copy(notificationChannel = channel) }
    }

    fun updateMonthlySavings(value: String) {
        // Убираем только лишние пробелы и разрешаем все символы
        val cleanedValue = value.trim()
        _state.update { it.copy(monthlySavings = cleanedValue) }
    }

    fun updateIncome(value: String) {
        // Убираем только лишние пробелы и разрешаем все символы
        val cleanedValue = value.trim()
        _state.update { it.copy(income = cleanedValue) }
    }

    fun updateConsiderSavings(value: Boolean) {
        _state.update { it.copy(considerSavings = value) }
    }

    fun updateCurrentSavings(value: String) {
        // Убираем только лишние пробелы и разрешаем все символы
        val cleanedValue = value.trim()
        _state.update { it.copy(currentSavings = cleanedValue) }
    }

    fun saveSettings(onSuccess: () -> Unit, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            val currentState = state.value

            // Проверяем валидность полей
            val errors = mutableListOf<String>()

            if (currentState.monthlySavings.isEmpty()) {
                errors.add("Введите сумму для ежемесячных накоплений")
            } else if (currentState.getMonthlySavingsAsInt() == null) {
                errors.add("Некорректная сумма для ежемесячных накоплений. Используйте только цифры")
            }

            if (currentState.income.isEmpty()) {
                errors.add("Введите ваш доход")
            } else if (currentState.getIncomeAsInt() == null) {
                errors.add("Некорректная сумма дохода. Используйте только цифры")
            }

            if (currentState.considerSavings && currentState.currentSavings.isEmpty()) {
                errors.add("Введите размер текущих накоплений")
            } else if (currentState.considerSavings && currentState.getCurrentSavingsAsInt() == null) {
                errors.add("Некорректная сумма текущих накоплений. Используйте только цифры")
            }

            if (errors.isEmpty()) {
                // Здесь можно сохранить в SharedPreferences или в базу данных
                saveToPreferences(
                    monthlySavings = currentState.getMonthlySavingsAsInt() ?: 0,
                    income = currentState.getIncomeAsInt() ?: 0,
                    considerSavings = currentState.considerSavings,
                    currentSavings = currentState.getCurrentSavingsAsInt() ?: 0,
                    notificationFrequency = currentState.notificationFrequency,
                    notificationChannel = currentState.notificationChannel
                )

                onSuccess()
            } else {
                onError(errors.joinToString("\n"))
            }
        }
    }

    private fun saveToPreferences(
        monthlySavings: Int,
        income: Int,
        considerSavings: Boolean,
        currentSavings: Int,
        notificationFrequency: NotificationFrequency,
        notificationChannel: NotificationChannel
    ) {
        // TODO: Реализовать сохранение в SharedPreferences
        // Пример:
        /*
        val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("monthly_savings", monthlySavings)
            putInt("income", income)
            putBoolean("consider_savings", considerSavings)
            putInt("current_savings", currentSavings)
            putString("notification_frequency", notificationFrequency.name)
            putString("notification_channel", notificationChannel.name)
            apply()
        }
        */
    }
}