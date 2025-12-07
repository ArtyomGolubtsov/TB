package com.example.tb.ui.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "shop_db", null, 1) {

    // Метод вызывается при создании базы данных
    override fun onCreate(db: SQLiteDatabase?) {
        // Создаем таблицу для категорий
        val createTypesTableQuery = """
            CREATE TABLE types (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL
            )
        """

        // Создаем таблицу для товаров
        val createProductsTableQuery = """
            CREATE TABLE products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                categoryId INTEGER,
                amount Double,
                link TEXT,
                isSave Integer,
                FOREIGN KEY (categoryId) REFERENCES types(id)
            )
        """

        // Выполнение SQL-запросов для создания таблиц
        db?.execSQL(createTypesTableQuery)
        db?.execSQL(createProductsTableQuery)
    }

    // Метод вызывается, если база данных обновляется
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Удаляем старые таблицы при обновлении базы данных
        db?.execSQL("DROP TABLE IF EXISTS types")
        db?.execSQL("DROP TABLE IF EXISTS products")
        onCreate(db)
    }
}
