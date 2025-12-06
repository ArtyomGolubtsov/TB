package com.example.tb.data

data class CoolingRule(
    val timeframe: String,      // "1 день", "1 неделя", "1 месяц"
    val amountRange: String,    // "до 15 000 ₽", "от 15 000 до 50 000 ₽", "от 50 000 ₽"
    val minAmount: Double,      // 0.0, 15000.0, 50000.0
    val maxAmount: Double?,     // 15000.0, 50000.0, null (если нет максимума)
    val targetDays: Int         // 1, 7, 30 дней
)

data class UserGoal(
    val id: String,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val selectedRule: CoolingRule? = null,
    val startDate: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)