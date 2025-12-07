package com.example.tb.ui.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

fun createDatabase(context: Context) {
    // Создаем экземпляр MyDatabaseHelper
    val dbHelper = MyDatabaseHelper(context)

    // Получаем доступ к базе данных (создаст или откроет существующую)
    val db = dbHelper.writableDatabase

    // Закрываем базу данных после создания
    db.close()
}

fun addCategory(context: Context, name: String) {
    val dbHelper = MyDatabaseHelper(context)
    val db = dbHelper.writableDatabase

    // Создаем объект для вставки данных
    val values = ContentValues().apply {
        put("name", name)
    }

    // Вставляем данные в таблицу types
    db.insert("types", null, values)

    db.close()
}

// Функция для добавления товара
fun addProduct(context: Context, name: String, categoryId: Int, price: Double, link: String) {
    val dbHelper = MyDatabaseHelper(context)
    val db = dbHelper.writableDatabase

    // Создаем объект для вставки данных
    val values = ContentValues().apply {
        put("name", name)
        put("categoryId", categoryId)
        put("price", price)
        put("link", link)
    }

    // Вставляем данные в таблицу products
    db.insert("products", null, values)

    db.close()
}

// Функция для получения всех категорий
fun getAllCategories(context: Context): List<String> {
    val dbHelper = MyDatabaseHelper(context)
    val db = dbHelper.readableDatabase

    // SQL-запрос для получения всех категорий
    val cursor: Cursor = db.rawQuery("SELECT name FROM types", null)

    val categories = mutableListOf<String>()
    while (cursor.moveToNext()) {
        val categoryName = cursor.getString(cursor.getColumnIndex("name"))
        categories.add(categoryName)
    }

    cursor.close()
    db.close()

    return categories
}

// Функция для получения всех товаров
fun getAllProducts(context: Context): List<String> {
    val dbHelper = MyDatabaseHelper(context)
    val db = dbHelper.readableDatabase

    // SQL-запрос для получения всех товаров
    val cursor: Cursor = db.rawQuery("SELECT name, price FROM products", null)

    val products = mutableListOf<String>()
    while (cursor.moveToNext()) {
        val productName = cursor.getString(cursor.getColumnIndex("name"))
        val price = cursor.getDouble(cursor.getColumnIndex("price"))
        products.add("Name: $productName, Price: $price")
    }

    cursor.close()
    db.close()

    return products
}