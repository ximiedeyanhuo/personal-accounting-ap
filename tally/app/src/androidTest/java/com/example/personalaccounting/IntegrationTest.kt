package com.example.personalaccounting

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
import com.example.personalaccounting.domain.service.StatisticsService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class IntegrationTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var statisticsService: StatisticsService

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        val transactionDao = TransactionDao(dbHelper)
        val categoryDao = CategoryDao(dbHelper)

        transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)
        statisticsService = StatisticsService(transactionRepository, categoryRepository)
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    @Test
    fun testFullWorkflow() {
        val foodCategory = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        val salaryCategory = Category(
            name = "Salary",
            type = TransactionType.INCOME,
            isDefault = true
        )

        val foodCategoryId = categoryRepository.insert(foodCategory)
        val salaryCategoryId = categoryRepository.insert(salaryCategory)

        val foodTransaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = foodCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        val salaryTransaction = Transaction(
            amount = 5000.0,
            type = TransactionType.INCOME,
            categoryId = salaryCategoryId,
            date = LocalDate.now(),
            note = "Monthly salary"
        )

        val foodTransactionId = transactionRepository.add(foodTransaction)
        val salaryTransactionId = transactionRepository.add(salaryTransaction)

        val retrievedFood = transactionRepository.getById(foodTransactionId)
        val retrievedSalary = transactionRepository.getById(salaryTransactionId)

        assertTrue(retrievedFood != null)
        assertTrue(retrievedSalary != null)
        assertEquals(100.0, retrievedFood?.amount)
        assertEquals(5000.0, retrievedSalary?.amount)

        val categoryExpenses = statisticsService.getCategoryExpenses(
            LocalDate.now().year,
            LocalDate.now().monthValue
        )

        assertEquals(1, categoryExpenses.size)
        assertEquals(100.0, categoryExpenses[0].totalAmount)

        val monthlySummary = statisticsService.getMonthlySummary(
            LocalDate.now().year,
            LocalDate.now().monthValue
        )

        assertEquals(5000.0, monthlySummary.totalIncome)
        assertEquals(100.0, monthlySummary.totalExpense)
        assertEquals(4900.0, monthlySummary.balance)
    }
}
