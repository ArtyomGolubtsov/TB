package com.example.tb.ui.screens.buyers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

enum class PurchaseStatus {
    ACTIVE,
    COMPLETED, // Купил - плохо, красный
    CANCELLED  // Отменил - хорошо, зеленый
}

data class Purchase(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String,
    val amount: Double,
    val link: String = "",
    val dateAdded: Long = System.currentTimeMillis(),
    val status: PurchaseStatus = PurchaseStatus.ACTIVE,
    val notificationsEnabled: Boolean = true,
    val cancelledAmount: Double = 0.0 // Сумма, которая накоплена при отмене
)

data class PurchaseState(
    val activePurchases: List<Purchase> = emptyList(),
    val completedPurchases: List<Purchase> = emptyList(),
    val cancelledPurchases: List<Purchase> = emptyList(),
    val allPurchases: List<Purchase> = emptyList(),
    val totalSaved: Double = 0.0, // Общая сумма сэкономленных денег
    val totalSpent: Double = 0.0  // Общая сумма потраченных денег
)

class PurchaseViewModel : ViewModel() {

    private val _state = MutableStateFlow(PurchaseState())
    val state: StateFlow<PurchaseState> = _state.asStateFlow()

    init {
        // Пример начальных данных
        viewModelScope.launch {
            val initialPurchases = listOf(
                Purchase(
                    title = "iPhone 15 Pro",
                    category = "Техника",
                    amount = 120000.0
                ),
                Purchase(
                    title = "iPhone 15 Pro",
                    category = "Техника",
                    amount = 120000.0
                ),
                Purchase(
                    title = "iPhone 15 Pro",
                    category = "Техника",
                    amount = 120000.0
                ),
                Purchase(
                    title = "Кроссовки Nike",
                    category = "Одежда",
                    amount = 8000.0
                ),
                Purchase(
                    title = "Путешествие в Турцию",
                    category = "Отдых",
                    amount = 50000.0
                )
            )

            _state.update { currentState ->
                val merged = currentState.activePurchases + initialPurchases
                currentState.copy(
                    activePurchases = merged,
                    allPurchases = merged
                )
            }
        }
    }

    fun addPurchase(title: String, category: String, amount: Double, link: String = "") {
        viewModelScope.launch {
            val newPurchase = Purchase(
                title = title,
                category = category,
                amount = amount,
                link = link
            )

            _state.update { currentState ->
                currentState.copy(
                    activePurchases = currentState.activePurchases + newPurchase,
                    allPurchases = currentState.allPurchases + newPurchase
                )
            }
        }
    }

    fun completePurchase(purchaseId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                val purchase = currentState.activePurchases.find { it.id == purchaseId }
                if (purchase != null) {
                    val updatedPurchase = purchase.copy(status = PurchaseStatus.COMPLETED)
                    val totalSpent = currentState.totalSpent + purchase.amount

                    currentState.copy(
                        activePurchases = currentState.activePurchases.filter { it.id != purchaseId },
                        completedPurchases = currentState.completedPurchases + updatedPurchase,
                        allPurchases = currentState.allPurchases.map {
                            if (it.id == purchaseId) updatedPurchase else it
                        },
                        totalSpent = totalSpent
                    )
                } else {
                    currentState
                }
            }
        }
    }

    // Отмена покупки - положительное действие (сэкономил деньги)
    fun cancelPurchase(purchaseId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                val purchase = currentState.activePurchases.find { it.id == purchaseId }
                if (purchase != null) {
                    val updatedPurchase = purchase.copy(
                        status = PurchaseStatus.CANCELLED,
                        cancelledAmount = purchase.amount
                    )
                    val totalSaved = currentState.totalSaved + purchase.amount

                    currentState.copy(
                        activePurchases = currentState.activePurchases.filter { it.id != purchaseId },
                        cancelledPurchases = currentState.cancelledPurchases + updatedPurchase,
                        allPurchases = currentState.allPurchases.map {
                            if (it.id == purchaseId) updatedPurchase else it
                        },
                        totalSaved = totalSaved
                    )
                } else {
                    currentState
                }
            }
        }
    }

    fun togglePurchaseNotifications(purchaseId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                val updatedAllPurchases = currentState.allPurchases.map { purchase ->
                    if (purchase.id == purchaseId) {
                        purchase.copy(notificationsEnabled = !purchase.notificationsEnabled)
                    } else {
                        purchase
                    }
                }

                val updatedActivePurchases = currentState.activePurchases.map { purchase ->
                    if (purchase.id == purchaseId) {
                        purchase.copy(notificationsEnabled = !purchase.notificationsEnabled)
                    } else {
                        purchase
                    }
                }

                currentState.copy(
                    activePurchases = updatedActivePurchases,
                    allPurchases = updatedAllPurchases
                )
            }
        }
    }

    fun formatAmount(amount: Double): String {
        return "%,d ₽".format(amount.toInt()).replace(',', ' ')
    }

    fun getProgressValue(purchase: Purchase): Float {
        // Сейчас заглушка, потом можно подвязать к реальному прогрессу
        return Random.nextFloat() * 100f
    }

    fun getPurchaseById(purchaseId: String): Purchase? {
        return _state.value.allPurchases.find { it.id == purchaseId }
    }

    // Баланс: сколько всего сэкономлено минус сколько потрачено
    fun getNetBalance(): Double {
        return _state.value.totalSaved - _state.value.totalSpent
    }

    fun getTotalSaved(): Double = _state.value.totalSaved

    fun getTotalSpent(): Double = _state.value.totalSpent
}
