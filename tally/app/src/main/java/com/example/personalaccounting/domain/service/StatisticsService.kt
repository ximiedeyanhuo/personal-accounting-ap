package com.example.personalaccounting.domain.service

import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.CategoryExpense

class StatisticsService(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) {

    fun getCategoryExpenses(year: Int, month: Int): List<CategoryExpense> {
        val expenses = transactionRepository.getMonthlyExpenses(year, month)
        val categories = categoryRepository.getAll()

        val categoryMap = categories.associateBy { it.id }

        val expensesByCategory = expenses.groupBy { it.categoryId }

        val totalExpenses = expenses.sumOf { it.amount }

        return expensesByCategory.map { (categoryId, transactions) ->
            val category = categoryMap[categoryId]
            val totalAmount = transactions.sumOf { it.amount }
            val percentage = if (totalExpenses > 0) (totalAmount / totalExpenses) * 100 else 0.0

            CategoryExpense(
                categoryId = categoryId,
                categoryName = category?.name ?: "Unknown",
                totalAmount = totalAmount,
                transactionCount = transactions.size,
                percentage = percentage
            )
        }.sortedByDescending { it.totalAmount }
    }

    fun getCategoryIncome(year: Int, month: Int): List<CategoryExpense> {
        val income = transactionRepository.getMonthlyIncome(year, month)
        val categories = categoryRepository.getAll()

        val categoryMap = categories.associateBy { it.id }

        val incomeByCategory = income.groupBy { it.categoryId }

        val totalIncome = income.sumOf { it.amount }

        return incomeByCategory.map { (categoryId, transactions) ->
            val category = categoryMap[categoryId]
            val totalAmount = transactions.sumOf { it.amount }
            val percentage = if (totalIncome > 0) (totalAmount / totalIncome) * 100 else 0.0

            CategoryExpense(
                categoryId = categoryId,
                categoryName = category?.name ?: "Unknown",
                totalAmount = totalAmount,
                transactionCount = transactions.size,
                percentage = percentage
            )
        }.sortedByDescending { it.totalAmount }
    }

    fun getMonthlySummary(year: Int, month: Int): MonthlySummary {
        val totalIncome = transactionRepository.getMonthlyTotalIncome(year, month)
        val totalExpense = transactionRepository.getMonthlyTotalExpense(year, month)
        val balance = totalIncome - totalExpense

        return MonthlySummary(
            year = year,
            month = month,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = balance
        )
    }
}

data class MonthlySummary(
    val year: Int,
    val month: Int,
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double
)
