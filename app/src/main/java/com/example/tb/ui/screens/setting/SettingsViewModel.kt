package com.example.tb.ui.screens.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tb.ui.notifications.NotificationScheduler
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
        val monthlySavingsValid = monthlySavings.isNotBlank()
        val incomeValid = income.isNotBlank()
        val currentSavingsValid = !considerSavings || currentSavings.isNotBlank()
        return monthlySavingsValid && incomeValid && currentSavingsValid
    }

    fun getMonthlySavingsAsInt(): Int? = monthlySavings.toIntOrNull()
    fun getIncomeAsInt(): Int? = income.toIntOrNull()
    fun getCurrentSavingsAsInt(): Int? = currentSavings.toIntOrNull()
}

/**
 * Частота напоминаний. Добавили длительность в миллисекундах.
 */
enum class NotificationFrequency(
    val displayName: String,
    val intervalMillis: Long
) {
    DAILY(
        "Ежедневно",
        24L * 60L * 60L * 1000L
    ),
    WEEKLY(
        "Раз в неделю",
        7L * 24L * 60L * 60L * 1000L
    ),
    BIWEEKLY(
        "Раз в две недели",
        14L * 24L * 60L * 60L * 1000L
    ),
    MONTHLY(
        "Раз в месяц",
        30L * 24L * 60L * 60L * 1000L
    )
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
        val cleanedValue = value.trim()
        _state.update { it.copy(monthlySavings = cleanedValue) }
    }

    fun updateIncome(value: String) {
        val cleanedValue = value.trim()
        _state.update { it.copy(income = cleanedValue) }
    }

    fun updateConsiderSavings(value: Boolean) {
        _state.update { it.copy(considerSavings = value) }
    }

    fun updateCurrentSavings(value: String) {
        val cleanedValue = value.trim()
        _state.update { it.copy(currentSavings = cleanedValue) }
    }

    /**
     * Загрузка настроек из SharedPreferences.
     * Вызывать один раз при открытии экрана.
     */
    fun loadSettings(context: Context) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

        val monthlySavings = prefs.getInt("monthly_savings", -1)
        val income = prefs.getInt("income", -1)
        val considerSavings = prefs.getBoolean("consider_savings", true)
        val currentSavings = prefs.getInt("current_savings", -1)

        val freqName = prefs.getString("notification_frequency", null)
        val channelName = prefs.getString("notification_channel", null)

        val frequency = freqName?.let {
            runCatching { NotificationFrequency.valueOf(it) }.getOrNull()
        } ?: NotificationFrequency.DAILY

        val channel = channelName?.let {
            runCatching { NotificationChannel.valueOf(it) }.getOrNull()
        } ?: NotificationChannel.PUSH

        _state.update { old ->
            old.copy(
                monthlySavings = if (monthlySavings >= 0) monthlySavings.toString() else "",
                income = if (income >= 0) income.toString() else "",
                considerSavings = considerSavings,
                currentSavings = if (currentSavings >= 0) currentSavings.toString() else "",
                notificationFrequency = frequency,
                notificationChannel = channel
            )
        }
    }

    /**
     * Сохраняем настройки в SharedPreferences + настраиваем напоминания.
     */
    fun saveSettings(
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val currentState = state.value
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

            if (errors.isNotEmpty()) {
                onError(errors.joinToString("\n"))
                return@launch
            }

            // 1. Сохраняем настройки
            saveToPreferences(
                context = context,
                monthlySavings = currentState.getMonthlySavingsAsInt() ?: 0,
                income = currentState.getIncomeAsInt() ?: 0,
                considerSavings = currentState.considerSavings,
                currentSavings = currentState.getCurrentSavingsAsInt() ?: 0,
                notificationFrequency = currentState.notificationFrequency,
                notificationChannel = currentState.notificationChannel
            )

            // 2. Настраиваем периодические уведомления
            NotificationScheduler.scheduleCoolingReminder(
                context = context,
                frequency = currentState.notificationFrequency
            )

            onSuccess()
        }
    }

    private fun saveToPreferences(
        context: Context,
        monthlySavings: Int,
        income: Int,
        considerSavings: Boolean,
        currentSavings: Int,
        notificationFrequency: NotificationFrequency,
        notificationChannel: NotificationChannel
    ) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putInt("monthly_savings", monthlySavings)
            putInt("income", income)
            putBoolean("consider_savings", considerSavings)
            putInt("current_savings", currentSavings)
            putString("notification_frequency", notificationFrequency.name)
            putString("notification_channel", notificationChannel.name)
            apply()
        }
    }
}
