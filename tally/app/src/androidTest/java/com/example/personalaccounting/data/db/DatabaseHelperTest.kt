package com.example.personalaccounting.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseHelperTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    @Test
    fun testDatabaseCreation() {
        assertTrue(db.isOpen)
    }

    @Test
    fun testTablesExist() {
        val cursor = db.rawQuery(
            "SELECT name FROM sqlite_master WHERE type='table' AND name IN ('transactions', 'categories')",
            null
        )
        assertTrue(cursor.count >= 2)
        cursor.close()
    }

    @Test
    fun testInsertAndQueryCategory() {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_CATEGORY_NAME, "Food")
            put(DatabaseHelper.COLUMN_CATEGORY_ICON, "ic_food")
            put(DatabaseHelper.COLUMN_CATEGORY_COLOR, "#FF0000")
            put(DatabaseHelper.COLUMN_CATEGORY_TYPE, "EXPENSE")
            put(DatabaseHelper.COLUMN_CATEGORY_IS_DEFAULT, 0)
            put(DatabaseHelper.COLUMN_CATEGORY_CREATED_AT, "2024-01-01T00:00:00")
        }
        val id = db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values)
        assertTrue(id > 0)

        val cursor = db.query(
            DatabaseHelper.TABLE_CATEGORIES,
            null,
            "${DatabaseHelper.COLUMN_CATEGORY_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        assertTrue(cursor.moveToFirst())
        assertEquals("Food", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)))
        cursor.close()
    }

    @Test
    fun testInsertAndQueryTransaction() {
        val categoryValues = ContentValues().apply {
            put(DatabaseHelper.COLUMN_CATEGORY_NAME, "Food")
            put(DatabaseHelper.COLUMN_CATEGORY_TYPE, "EXPENSE")
            put(DatabaseHelper.COLUMN_CATEGORY_IS_DEFAULT, 0)
            put(DatabaseHelper.COLUMN_CATEGORY_CREATED_AT, "2024-01-01T00:00:00")
        }
        val categoryId = db.insert(DatabaseHelper.TABLE_CATEGORIES, null, categoryValues)
        assertTrue(categoryId > 0)

        val transactionValues = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TRANSACTION_AMOUNT, 50.0)
            put(DatabaseHelper.COLUMN_TRANSACTION_TYPE, "EXPENSE")
            put(DatabaseHelper.COLUMN_TRANSACTION_CATEGORY_ID, categoryId)
            put(DatabaseHelper.COLUMN_TRANSACTION_DATE, "2024-01-15")
            put(DatabaseHelper.COLUMN_TRANSACTION_NOTE, "Lunch")
            put(DatabaseHelper.COLUMN_TRANSACTION_CREATED_AT, "2024-01-15T12:00:00")
            put(DatabaseHelper.COLUMN_TRANSACTION_UPDATED_AT, "2024-01-15T12:00:00")
        }
        val txId = db.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, transactionValues)
        assertTrue(txId > 0)

        val cursor = db.query(
            DatabaseHelper.TABLE_TRANSACTIONS,
            null,
            "${DatabaseHelper.COLUMN_TRANSACTION_ID} = ?",
            arrayOf(txId.toString()),
            null, null, null
        )
        assertTrue(cursor.moveToFirst())
        assertEquals(50.0, cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_AMOUNT)), 0.001)
        assertEquals("Lunch", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_NOTE)))
        cursor.close()
    }

    @Test
    fun testForeignKeyViolation() {
        val transactionValues = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TRANSACTION_AMOUNT, 25.0)
            put(DatabaseHelper.COLUMN_TRANSACTION_TYPE, "EXPENSE")
            put(DatabaseHelper.COLUMN_TRANSACTION_CATEGORY_ID, 99999)
            put(DatabaseHelper.COLUMN_TRANSACTION_DATE, "2024-01-15")
            put(DatabaseHelper.COLUMN_TRANSACTION_CREATED_AT, "2024-01-15T12:00:00")
            put(DatabaseHelper.COLUMN_TRANSACTION_UPDATED_AT, "2024-01-15T12:00:00")
        }
        val txId = db.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, transactionValues)
        // foreign key violation should prevent insert when PRAGMA foreign_keys=ON
        assertEquals(-1L, txId)
    }

    private fun assertEquals(expected: Any?, actual: Any?) {
        org.junit.Assert.assertEquals(expected, actual)
    }

    private fun assertEquals(expected: Double, actual: Double, delta: Double) {
        org.junit.Assert.assertEquals(expected, actual, delta)
    }
}
