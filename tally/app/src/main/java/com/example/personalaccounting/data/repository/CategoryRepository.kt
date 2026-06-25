package com.example.personalaccounting.data.repository

import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType

class CategoryRepository(private val categoryDao: CategoryDao) {

    fun insert(category: Category): Long {
        return categoryDao.insert(category)
    }

    fun getById(id: Long): Category? {
        return categoryDao.getById(id)
    }

    fun getAll(): List<Category> {
        return categoryDao.getAll()
    }

    fun getByType(type: TransactionType): List<Category> {
        return categoryDao.getByType(type)
    }

    fun update(category: Category): Int {
        return categoryDao.update(category)
    }

    fun delete(id: Long): Int {
        return categoryDao.delete(id)
    }

    fun createDefaultCategories() {
        val defaultCategories = listOf(
            Category(name = "Food", type = TransactionType.EXPENSE, isDefault = true),
            Category(name = "Transport", type = TransactionType.EXPENSE, isDefault = true),
            Category(name = "Shopping", type = TransactionType.EXPENSE, isDefault = true),
            Category(name = "Entertainment", type = TransactionType.EXPENSE, isDefault = true),
            Category(name = "Salary", type = TransactionType.INCOME, isDefault = true),
            Category(name = "Investment", type = TransactionType.INCOME, isDefault = true)
        )

        defaultCategories.forEach { category ->
            categoryDao.insert(category)
        }
    }
}
