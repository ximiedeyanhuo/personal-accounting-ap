package com.example.personalaccounting.data.repository

import com.example.personalaccounting.data.db.TransactionDao
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import java.time.LocalDate

class TransactionRepository(private val transactionDao: TransactionDao) {

    fun add(transaction: Transaction): Long {
        return transactionDao.insert(transaction)
    }

    fun getById(id: Long): Transaction? {
        return transactionDao.getById(id)
    }

    fun getAll(): List<Transaction> {
        return transactionDao.getAll()
    }

    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): List<Transaction> {
        return transactionDao.getByDateRange(startDate, endDate)
    }

    fun getByType(type: TransactionType): List<Transaction> {
        return transactionDao.getByType(type)
    }

    fun update(transaction: Transaction): Int {
        return transactionDao.update(transaction)
    }

    fun delete(id: Long): Int {
        return transactionDao.delete(id)
    }

    fun getMonthlyTransactions(year: Int, month: Int): List<Transaction> {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = startDate.plusMonths(1).minusDays(1)
        return transactionDao.getByDateRange(startDate, endDate)
    }

    fun getMonthlyExpenses(year: Int, month: Int): List<Transaction> {
        return getMonthlyTransactions(year, month).filter { it.type == TransactionType.EXPENSE }
    }

    fun getMonthlyIncome(year: Int, month: Int): List<Transaction> {
        return getMonthlyTransactions(year, month).filter { it.type == TransactionType.INCOME }
    }

    fun getMonthlyTotalExpense(year: Int, month: Int): Double {
        return getMonthlyExpenses(year, month).sumOf { it.amount }
    }

    fun getMonthlyTotalIncome(year: Int, month: Int): Double {
        return getMonthlyIncome(year, month).sumOf { it.amount }
    }

    fun getMonthlyBalance(year: Int, month: Int): Double {
        return getMonthlyTotalIncome(year, month) - getMonthlyTotalExpense(year, month)
    }
}
