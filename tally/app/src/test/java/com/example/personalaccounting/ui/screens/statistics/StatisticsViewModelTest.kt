package com.example.personalaccounting.ui.screens.statistics

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

@RunWith(AndroidJUnit4::class)
class StatisticsViewModelTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var viewModel: StatisticsViewModel
    private lateinit var statisticsService: StatisticsService
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var transactionRepository: TransactionRepository
    private var testCategoryId: Long = 0

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        val transactionDao = TransactionDao(dbHelper)
        val categoryDao = CategoryDao(dbHelper)

        transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)
        statisticsService = StatisticsService(transactionRepository, categoryRepository)

        viewModel = StatisticsViewModel(statisticsService, categoryRepository)

        val category = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        testCategoryId = categoryRepository.insert(category)

        val transaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        transactionRepository.add(transaction)
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    @Test
    fun testLoadCategoryExpenses() {
        viewModel.loadCategoryExpenses()

        assertEquals(1, viewModel.categoryExpenses.value.size)
        assertEquals(100.0, viewModel.categoryExpenses.value[0].totalAmount)
    }
}
