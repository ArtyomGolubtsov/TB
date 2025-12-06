package com.example.tb.ui.screens.buyers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

data class Purchase(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String,
    val amount: Double,
    val link: String = "",
    val dateAdded: Long = System.currentTimeMillis(),
    val isActive: Boolean = true // true - активная покупка, false - завершенная (в истории)
)

data class PurchaseState(
    val activePurchases: List<Purchase> = emptyList(),
    val completedPurchases: List<Purchase> = emptyList(),
    val allPurchases: List<Purchase> = emptyList()
)

class PurchaseViewModel : ViewModel() {
    private val _state = MutableStateFlow(PurchaseState())
    val state: StateFlow<PurchaseState> = _state.asStateFlow()

    init {
        // Заполняем начальными данными для примера
        viewModelScope.launch {
            val initialPurchases = listOf(
                Purchase(
                    title = "iPhone 15 Pro",
                    category = "Техника",
                    amount = 120000.0
                ),
                Purchase(
                    title = "Кроссовки Nike",
                    category = "Одежда",
                    amount = 8000.0
                )
            )
            _state.update { it.copy(activePurchases = initialPurchases) }
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
                    val updatedPurchase = purchase.copy(isActive = false)
                    currentState.copy(
                        activePurchases = currentState.activePurchases.filter { it.id != purchaseId },
                        completedPurchases = currentState.completedPurchases + updatedPurchase,
                        allPurchases = currentState.allPurchases.map {
                            if (it.id == purchaseId) updatedPurchase else it
                        }
                    )
                } else {
                    currentState
                }
            }
        }
    }

    fun formatAmount(amount: Double): String {
        return "%,d ₽".format(amount.toInt()).replace(',', ' ')
    }

    fun getProgressValue(purchase: Purchase): Float {
        // Просто пример - можно сделать реальную логику
        return Random.nextFloat() * 100f
    }
}