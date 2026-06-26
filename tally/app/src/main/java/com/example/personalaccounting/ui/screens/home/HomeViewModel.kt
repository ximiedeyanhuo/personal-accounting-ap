package com.example.personalaccounting.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalaccounting.DIContainer
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.service.MonthlySummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel : ViewModel() {

    private val transactionRepository = DIContainer.getTransactionRepository()
    private val categoryRepository = DIContainer.getCategoryRepository()
    private val statisticsService = DIContainer.getStatisticsService()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _monthlySummary = MutableStateFlow<MonthlySummary?>(null)
    val monthlySummary: StateFlow<MonthlySummary?> = _monthlySummary.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadTransactions()
        loadCategories()
        loadMonthlySummary()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _transactions.value = transactionRepository.getAll()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getAll()
        }
    }

    fun loadMonthlySummary() {
        viewModelScope.launch {
            val now = LocalDate.now()
            _monthlySummary.value = statisticsService.getMonthlySummary(now.year, now.monthValue)
        }
    }

    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            transactionRepository.delete(transactionId)
            loadTransactions()
            loadMonthlySummary()
        }
    }

    fun getCategoryById(categoryId: Long): Category? {
        return _categories.value.find { it.id == categoryId }
    }
}
