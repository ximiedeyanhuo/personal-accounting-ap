package com.example.personalaccounting.data.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        categoryDao = CategoryDao(dbHelper)
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    @Test
    fun testInsertCategory() {
        val category = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        val id = categoryDao.insert(category)
        assertTrue(id > 0)
    }

    @Test
    fun testGetCategoryById() {
        val category = Category(
            name = "Salary",
            type = TransactionType.INCOME,
            isDefault = false
        )
        val id = categoryDao.insert(category)
        val retrieved = categoryDao.getById(id)

        assertEquals("Salary", retrieved?.name)
        assertEquals(TransactionType.INCOME, retrieved?.type)
        assertEquals(false, retrieved?.isDefault)
    }

    @Test
    fun testGetAllCategories() {
        val c1 = Category(name = "Food", type = TransactionType.EXPENSE, isDefault = true)
        val c2 = Category(name = "Salary", type = TransactionType.INCOME, isDefault = false)
        categoryDao.insert(c1)
        categoryDao.insert(c2)

        val all = categoryDao.getAll()
        assertEquals(2, all.size)
    }

    @Test
    fun testGetByType() {
        val c1 = Category(name = "Food", type = TransactionType.EXPENSE, isDefault = true)
        val c2 = Category(name = "Salary", type = TransactionType.INCOME, isDefault = false)
        categoryDao.insert(c1)
        categoryDao.insert(c2)

        val expenses = categoryDao.getByType(TransactionType.EXPENSE)
        val incomes = categoryDao.getByType(TransactionType.INCOME)
        assertEquals(1, expenses.size)
        assertEquals(TransactionType.EXPENSE, expenses[0].type)
        assertEquals(1, incomes.size)
        assertEquals(TransactionType.INCOME, incomes[0].type)
    }

    @Test
    fun testUpdateCategory() {
        val category = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        val id = categoryDao.insert(category)

        val updated = category.copy(id = id, name = "Groceries")
        val rows = categoryDao.update(updated)
        assertEquals(1, rows)

        val retrieved = categoryDao.getById(id)
        assertEquals("Groceries", retrieved?.name)
    }

    @Test
    fun testDeleteCategory() {
        val category = Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        )
        val id = categoryDao.insert(category)
        val rows = categoryDao.delete(id)
        assertEquals(1, rows)
        assertNull(categoryDao.getById(id))
    }

    @Test
    fun testGetByIdReturnsNullForNonexistent() {
        assertNull(categoryDao.getById(99999L))
    }
}
