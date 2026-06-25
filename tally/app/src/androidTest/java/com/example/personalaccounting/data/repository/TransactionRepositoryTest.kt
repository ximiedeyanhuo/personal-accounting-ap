package com.example.personalaccounting.data.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class TransactionRepositoryTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private var testCategoryId: Long = 0

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        val transactionDao = TransactionDao(dbHelper)
        val categoryDao = CategoryDao(dbHelper)

        transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)

        val category = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        testCategoryId = categoryRepository.insert(category)
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    @Test
    fun testAddTransaction() {
        val transaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        val id = transactionRepository.add(transaction)
        assertTrue(id > 0)
    }

    @Test
    fun testGetMonthlyExpenses() {
        val transaction1 = Transaction(
            amount = 50.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Breakfast"
        )
        val transaction2 = Transaction(
            amount = 30.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Snack"
        )

        transactionRepository.add(transaction1)
        transactionRepository.add(transaction2)

        val monthlyExpenses = transactionRepository.getMonthlyExpenses(LocalDate.now().year, LocalDate.now().monthValue)
        assertEquals(2, monthlyExpenses.size)
    }
}
