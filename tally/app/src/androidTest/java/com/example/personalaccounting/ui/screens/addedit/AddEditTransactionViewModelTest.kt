package com.example.personalaccounting.ui.screens.addedit

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType
import com.example.personalaccounting.rule.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class AddEditTransactionViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var viewModel: AddEditTransactionViewModel
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

        viewModel = AddEditTransactionViewModel(
            transactionRepository,
            categoryRepository
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
    fun testSaveTransaction() = runTest {
        viewModel.updateAmount("100.0")
        viewModel.updateType(TransactionType.EXPENSE)
        viewModel.updateCategoryId(testCategoryId)
        viewModel.updateDate(LocalDate.now())
        viewModel.updateNote("Lunch")

        viewModel.saveTransaction()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.isSuccess.value)
    }

    @Test
    fun testValidation() {
        viewModel.updateAmount("")
        viewModel.updateType(TransactionType.EXPENSE)
        viewModel.updateCategoryId(testCategoryId)
        viewModel.updateDate(LocalDate.now())

        viewModel.saveTransaction()

        assertEquals("Amount is required", viewModel.errorMessage.value)
    }
}
