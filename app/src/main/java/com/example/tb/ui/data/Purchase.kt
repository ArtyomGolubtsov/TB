package com.example.tb.ui.data

import java.util.*

data class Purchase(
    val id: String = UUID.randomUUID().toString(),  // Уникальный идентификатор
    val title: String,                             // Название товара
    val category: String,                          // Категория товара
    val amount: Double,                            // Сумма товара
    val link: String = ""                          // Ссылка на товар (если есть)
)
