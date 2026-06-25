package com.example.personalaccounting.ui.screens.home

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
import com.example.personalaccounting.rule.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var viewModel: HomeViewModel
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var statisticsService: StatisticsService
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
        
        viewModel = HomeViewModel(
            transactionRepository,
            categoryRepository,
            statisticsService
        )
        
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
    fun testLoadTransactions() = runTest {
        val transaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        transactionRepository.add(transaction)
        
        viewModel.loadTransactions()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(1, viewModel.transactions.value.size)
    }
    
    @Test
    fun testLoadMonthlySummary() = runTest {
        val transaction = Transaction(
            amount = 100.0,
            type = TransactionType.EXPENSE,
            categoryId = testCategoryId,
            date = LocalDate.now(),
            note = "Lunch"
        )
        transactionRepository.add(transaction)
        
        viewModel.loadMonthlySummary()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(100.0, viewModel.monthlySummary.value?.totalExpense)
    }
}
