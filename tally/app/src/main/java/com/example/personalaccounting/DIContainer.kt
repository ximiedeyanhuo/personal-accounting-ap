package com.example.personalaccounting

import android.content.Context
import com.example.personalaccounting.data.db.CategoryDao
import com.example.personalaccounting.data.db.DatabaseHelper
import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.service.StatisticsService

object DIContainer {
    private var dbHelper: DatabaseHelper? = null
    private var transactionRepository: TransactionRepository? = null
    private var categoryRepository: CategoryRepository? = null
    private var statisticsService: StatisticsService? = null

    fun init(context: Context) {
        dbHelper = DatabaseHelper(context)
        val transactionDao = TransactionDao(dbHelper!!)
        val categoryDao = CategoryDao(dbHelper!!)
        transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)
        statisticsService = StatisticsService(transactionRepository!!, categoryRepository!!)
    }

    fun getTransactionRepository(): TransactionRepository {
        return transactionRepository ?: throw IllegalStateException("DIContainer not initialized")
    }

    fun getCategoryRepository(): CategoryRepository {
        return categoryRepository ?: throw IllegalStateException("DIContainer not initialized")
    }

    fun getStatisticsService(): StatisticsService {
        return statisticsService ?: throw IllegalStateException("DIContainer not initialized")
    }
}
