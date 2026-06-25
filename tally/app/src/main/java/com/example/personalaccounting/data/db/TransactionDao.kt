package com.example.personalaccounting.data.db

import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import java.time.LocalDate
import java.time.LocalDateTime

class TransactionDao(private val dbHelper: DatabaseHelper) {

    fun insert(transaction: Transaction): Long {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put(DatabaseHelper.COLUMN_TRANSACTION_AMOUNT, transaction.amount)
            put(DatabaseHelper.COLUMN_TRANSACTION_TYPE, transaction.type.name)
            put(DatabaseHelper.COLUMN_TRANSACTION_CATEGORY_ID, transaction.categoryId)
            put(DatabaseHelper.COLUMN_TRANSACTION_DATE, transaction.date.toString())
            put(DatabaseHelper.COLUMN_TRANSACTION_NOTE, transaction.note)
            put(DatabaseHelper.COLUMN_TRANSACTION_CREATED_AT, transaction.createdAt.toString())
            put(DatabaseHelper.COLUMN_TRANSACTION_UPDATED_AT, transaction.updatedAt.toString())
        }
        return db.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, values)
    }

    fun getById(id: Long): Transaction? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_TRANSACTIONS,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            cursorToTransaction(cursor)
        } else {
            null
        }.also {
            cursor.close()
        }
    }

    fun getAll(): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_TRANSACTIONS,
            null,
            null,
            null,
            null,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_DATE} DESC"
        )

        while (cursor.moveToNext()) {
            transactions.add(cursorToTransaction(cursor))
        }
        cursor.close()
        return transactions
    }

    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_TRANSACTIONS,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_DATE} BETWEEN ? AND ?",
            arrayOf(startDate.toString(), endDate.toString()),
            null,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_DATE} DESC"
        )

        while (cursor.moveToNext()) {
            transactions.add(cursorToTransaction(cursor))
        }
        cursor.close()
        return transactions
    }

    fun getByType(type: TransactionType): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_TRANSACTIONS,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_TYPE} = ?",
            arrayOf(type.name),
            null,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_DATE} DESC"
        )

        while (cursor.moveToNext()) {
            transactions.add(cursorToTransaction(cursor))
        }
        cursor.close()
        return transactions
    }

    fun update(transaction: Transaction): Int {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put(DatabaseHelper.COLUMN_TRANSACTION_AMOUNT, transaction.amount)
            put(DatabaseHelper.COLUMN_TRANSACTION_TYPE, transaction.type.name)
            put(DatabaseHelper.COLUMN_TRANSACTION_CATEGORY_ID, transaction.categoryId)
            put(DatabaseHelper.COLUMN_TRANSACTION_DATE, transaction.date.toString())
            put(DatabaseHelper.COLUMN_TRANSACTION_NOTE, transaction.note)
            put(DatabaseHelper.COLUMN_TRANSACTION_UPDATED_AT, LocalDateTime.now().toString())
        }
        return db.update(
            DatabaseHelper.TABLE_TRANSACTIONS,
            values,
            "${DatabaseHelper.COLUMN_TRANSACTION_ID} = ?",
            arrayOf(transaction.id.toString())
        )
    }

    fun delete(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseHelper.TABLE_TRANSACTIONS,
            "${DatabaseHelper.COLUMN_TRANSACTION_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun cursorToTransaction(cursor: android.database.Cursor): Transaction {
        return Transaction(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_ID)),
            amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_AMOUNT)),
            type = TransactionType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_TYPE))),
            categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_CATEGORY_ID)),
            date = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_DATE))),
            note = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_NOTE)) ?: "",
            createdAt = LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_CREATED_AT))),
            updatedAt = LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_UPDATED_AT)))
        )
    }
}
