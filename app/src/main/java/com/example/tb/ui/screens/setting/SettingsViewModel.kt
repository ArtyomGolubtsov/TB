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
        return monthlySavings.isNotBlank() && income.isNotBlank() &&
                (monthlySavings.all { it.isDigit() } && income.all { it.isDigit() }) &&
                (!considerSavings || (considerSavings && currentSavings.isNotBlank() && currentSavings.all { it.isDigit() }))
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
        val filtered = value.filter { it.isDigit() }
        _state.update { it.copy(monthlySavings = filtered) }
    }

    fun updateIncome(value: String) {
        val filtered = value.filter { it.isDigit() }
        _state.update { it.copy(income = filtered) }
    }

    fun updateConsiderSavings(value: Boolean) {
        _state.update { it.copy(considerSavings = value) }
    }

    fun updateCurrentSavings(value: String) {
        val filtered = value.filter { it.isDigit() }
        _state.update { it.copy(currentSavings = filtered) }
    }

    fun saveSettings(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (state.value.isValid()) {
                // Здесь можно сохранить в SharedPreferences или в базу данных
                // Например: repository.saveSettings(state.value)

                onSuccess()
            } else {
                // Показать ошибку валидации
            }
        }
    }
}