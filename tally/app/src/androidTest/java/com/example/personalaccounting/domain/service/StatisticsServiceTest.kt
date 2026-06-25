package com.example.personalaccounting.domain.service

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class StatisticsServiceTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var statisticsService: StatisticsService
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private var foodCategoryId: Long = 0
    private var transportCategoryId: Long = 0

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        val transactionDao = TransactionDao(dbHelper)
        val categoryDao = CategoryDao(dbHelper)

        transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)
        statisticsService = StatisticsService(transactionRepository, categoryRepository)

        val foodCategory = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        val transportCategory = Category(
            name = "Transport",
            type = TransactionType.EXPENSE,
            isDefault = true
        )

        foodCategoryId = categoryRepository.insert(foodCategory)
        transportCategoryId = categoryRepository.insert(transportCategory)

        val transaction1 = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = foodCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        val transaction2 = Transaction(
            amount = 50.0,
            type = TransactionType.EXPENSE,
            categoryId = foodCategoryId,
            date = LocalDate.now(),
            note = "Dinner"
        )
        val transaction3 = Transaction(
            amount = 30.0,
            type = TransactionType.EXPENSE,
            categoryId = transportCategoryId,
            date = LocalDate.now(),
            note = "Bus"
        )

        transactionRepository.add(transaction1)
        transactionRepository.add(transaction2)
        transactionRepository.add(transaction3)
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    @Test
    fun testGetCategoryExpenses() {
        val categoryExpenses = statisticsService.getCategoryExpenses(
            LocalDate.now().year,
            LocalDate.now().monthValue
        )

        assertEquals(2, categoryExpenses.size)

        val foodExpense = categoryExpenses.find { it.categoryId == foodCategoryId }
        assertEquals(150.0, foodExpense?.totalAmount)
        assertEquals(2, foodExpense?.transactionCount)

        val transportExpense = categoryExpenses.find { it.categoryId == transportCategoryId }
        assertEquals(30.0, transportExpense?.totalAmount)
        assertEquals(1, transportExpense?.transactionCount)
    }
}
