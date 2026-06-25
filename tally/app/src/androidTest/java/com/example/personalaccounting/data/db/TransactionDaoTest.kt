package com.example.personalaccounting.data.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class TransactionDaoTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var transactionDao: TransactionDao
    private lateinit var categoryDao: CategoryDao
    private var testCategoryId: Long = 0

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        transactionDao = TransactionDao(dbHelper)
        categoryDao = CategoryDao(dbHelper)

        val category = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        testCategoryId = categoryDao.insert(category)
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    @Test
    fun testInsertTransaction() {
        val transaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        val id = transactionDao.insert(transaction)
        assertTrue(id > 0)
    }

    @Test
    fun testGetTransactionById() {
        val transaction = Transaction(
            amount = 50.0,
            type = TransactionType.INCOME,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Salary"
        )
        val id = transactionDao.insert(transaction)
        val retrieved = transactionDao.getById(id)

        assertEquals(50.0, retrieved?.amount, 0.001)
        assertEquals(TransactionType.INCOME, retrieved?.type)
    }

    @Test
    fun testGetAllTransactions() {
        val t1 = Transaction(amount = 10.0, type = TransactionType.EXPENSE, categoryId = testCategoryId, date = LocalDate.now())
        val t2 = Transaction(amount = 20.0, type = TransactionType.INCOME, categoryId = testCategoryId, date = LocalDate.now())
        transactionDao.insert(t1)
        transactionDao.insert(t2)

        val all = transactionDao.getAll()
        assertEquals(2, all.size)
    }

    @Test
    fun testUpdateTransaction() {
        val transaction = Transaction(
            amount = 30.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Old"
        )
        val id = transactionDao.insert(transaction)

        val updated = transaction.copy(id = id, note = "New")
        val rows = transactionDao.update(updated)
        assertEquals(1, rows)

        val retrieved = transactionDao.getById(id)
        assertEquals("New", retrieved?.note)
    }

    @Test
    fun testDeleteTransaction() {
        val transaction = Transaction(
            amount = 40.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now()
        )
        val id = transactionDao.insert(transaction)
        val rows = transactionDao.delete(id)
        assertEquals(1, rows)
        assertTrue(transactionDao.getById(id) == null)
    }
}
