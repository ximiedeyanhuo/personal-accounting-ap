package com.example.personalaccounting.data.db

import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType

class CategoryDao(private val dbHelper: DatabaseHelper) {

    fun insert(category: Category): Long {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.name)
            put(DatabaseHelper.COLUMN_CATEGORY_ICON, category.icon)
            put(DatabaseHelper.COLUMN_CATEGORY_COLOR, category.color)
            put(DatabaseHelper.COLUMN_CATEGORY_TYPE, category.type.name)
            put(DatabaseHelper.COLUMN_CATEGORY_IS_DEFAULT, if (category.isDefault) 1 else 0)
            put(DatabaseHelper.COLUMN_CATEGORY_CREATED_AT, category.createdAt.toString())
        }
        return db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values)
    }

    fun getById(id: Long): Category? {
        val db = dbHelper.readableDatabase
        return db.query(
            DatabaseHelper.TABLE_CATEGORIES,
            null,
            "${DatabaseHelper.COLUMN_CATEGORY_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use { cursor ->
            if (cursor.moveToFirst()) cursorToCategory(cursor) else null
        }
    }

    fun getAll(): List<Category> {
        val db = dbHelper.readableDatabase
        val categories = mutableListOf<Category>()
        db.query(
            DatabaseHelper.TABLE_CATEGORIES,
            null,
            null,
            null,
            null,
            null,
            "${DatabaseHelper.COLUMN_CATEGORY_NAME} ASC"
        ).use { cursor ->
            while (cursor.moveToNext()) {
                categories.add(cursorToCategory(cursor))
            }
        }
        return categories
    }

    fun getByType(type: TransactionType): List<Category> {
        val db = dbHelper.readableDatabase
        val categories = mutableListOf<Category>()
        db.query(
            DatabaseHelper.TABLE_CATEGORIES,
            null,
            "${DatabaseHelper.COLUMN_CATEGORY_TYPE} = ?",
            arrayOf(type.name),
            null,
            null,
            "${DatabaseHelper.COLUMN_CATEGORY_NAME} ASC"
        ).use { cursor ->
            while (cursor.moveToNext()) {
                categories.add(cursorToCategory(cursor))
            }
        }
        return categories
    }

    fun update(category: Category): Int {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put(DatabaseHelper.COLUMN_CATEGORY_NAME, category.name)
            put(DatabaseHelper.COLUMN_CATEGORY_ICON, category.icon)
            put(DatabaseHelper.COLUMN_CATEGORY_COLOR, category.color)
            put(DatabaseHelper.COLUMN_CATEGORY_TYPE, category.type.name)
            put(DatabaseHelper.COLUMN_CATEGORY_IS_DEFAULT, if (category.isDefault) 1 else 0)
        }
        return db.update(
            DatabaseHelper.TABLE_CATEGORIES,
            values,
            "${DatabaseHelper.COLUMN_CATEGORY_ID} = ?",
            arrayOf(category.id.toString())
        )
    }

    fun delete(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseHelper.TABLE_CATEGORIES,
            "${DatabaseHelper.COLUMN_CATEGORY_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun cursorToCategory(cursor: android.database.Cursor): Category {
        return Category(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)),
            icon = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ICON)) ?: "",
            color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_COLOR)) ?: "",
            type = TransactionType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_TYPE))),
            isDefault = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_IS_DEFAULT)) == 1,
            createdAt = java.time.LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_CREATED_AT)))
        )
    }
}
