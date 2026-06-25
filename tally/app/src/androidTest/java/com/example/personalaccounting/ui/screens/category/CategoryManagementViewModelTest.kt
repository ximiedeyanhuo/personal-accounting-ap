package com.example.personalaccounting.ui.screens.category

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.domain.model.TransactionType
import com.example.personalaccounting.rule.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class CategoryManagementViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var viewModel: CategoryManagementViewModel
    private lateinit var categoryRepository: CategoryRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        val categoryDao = CategoryDao(dbHelper)

        categoryRepository = CategoryRepository(categoryDao)
        viewModel = CategoryManagementViewModel(categoryRepository)
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    @Test
    fun testAddCategory() = runTest {
        viewModel.updateName("Food")
        viewModel.updateType(TransactionType.EXPENSE)

        viewModel.addCategory()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.categories.value.size)
        assertEquals("Food", viewModel.categories.value[0].name)
    }

    @Test
    fun testValidation() {
        viewModel.updateName("")
        viewModel.updateType(TransactionType.EXPENSE)

        viewModel.addCategory()

        assertEquals("Category name is required", viewModel.errorMessage.value)
    }
}
